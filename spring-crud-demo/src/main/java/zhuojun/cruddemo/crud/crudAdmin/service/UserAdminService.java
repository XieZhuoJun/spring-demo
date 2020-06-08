package zhuojun.cruddemo.crud.crudadmin.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import zhuojun.cruddemo.crud.auth.mapper.UserMapper;
import zhuojun.cruddemo.crud.common.constant.Constants;
import zhuojun.cruddemo.crud.common.domain.*;
import zhuojun.cruddemo.crud.common.enums.MessageEnum;
import zhuojun.cruddemo.crud.common.enums.RoleEnum;
import zhuojun.cruddemo.crud.common.exception.AuthenticationException;
import zhuojun.cruddemo.crud.common.exception.InternalException;
import zhuojun.cruddemo.crud.common.exception.RegisterException;
import zhuojun.cruddemo.crud.common.util.ClassUtil;
import zhuojun.cruddemo.crud.common.util.RedisUtil;
import zhuojun.cruddemo.crud.crudadmin.domain.dto.RegisterForm;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/05/30 13:36
 * @modified:
 */
@Slf4j
@Service
public class UserAdminService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtil redisUtil;

    /**
     * @param registerForm
     * @return
     */
    public Result register(RegisterForm registerForm) {
        User newUser = new User();
        newUser.setEmail(registerForm.getEmail())
                .setUsername(registerForm.getUsername())
                .setRoleId(RoleEnum.USER.getRoleId())
                .setCreateTime(LocalDateTime.now());

        /*
        检查用户是否已存在
         */
        Map<String, Object> selectMap = new HashMap<>(0);
        selectMap.put("username", newUser.getUsername());
        if (!userMapper.selectByMap(selectMap).isEmpty()) {
            throw new RegisterException(MessageEnum.ACCOUNT_EXIST.getMsg());
        }
        selectMap.clear();
        selectMap.put("email", newUser.getEmail());
        if (!userMapper.selectByMap(selectMap).isEmpty()) {
            throw new RegisterException(MessageEnum.ACCOUNT_EXIST.getMsg());
        }

        /*
        使用BCrypt加密密码后放入数据库
         */
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        newUser.setPassword(passwordEncoder.encode(registerForm.getPassword()));
        if (userMapper.insert(newUser) == 0) {
            throw new AuthenticationException(MessageEnum.ERROR.getMsg());
        } else {
            return Result.successResult(MessageEnum.SUCCESS.getMsg());
        }
    }

    /**
     * @param pattern
     * @return
     */
    private Set<TokenView> getTokenViews(String pattern) {
        Set<String> keySet = redisUtil.keys(pattern);
        Set<TokenView> tokenViewSet = new HashSet<>();
        if (keySet != null) {
            for (String key : keySet) {
                try {
                    TokenView tokenView = new TokenView();
                    RedisTokenValue tokenValue = (RedisTokenValue) redisUtil.get(key);
                    DecodedJWT jwt = JWT.decode(tokenValue.getToken());
                    tokenView.setUuid(jwt.getClaim(Constants.UUID_CLAIM_KEY).asString())
                            .setRoleId(jwt.getClaim(Constants.ROLE_CLAIM_KEY).asInt())
                            .setUserId(Long.valueOf(jwt.getAudience().get(0)))
                            .setExpireTime(LocalDateTime.ofInstant(jwt.getExpiresAt().toInstant(), ZoneId.systemDefault()))
                            .setPlatformId(jwt.getClaim(Constants.PLATFORM_CLAIM_KEY).asInt());
                    tokenViewSet.add(tokenView);
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            }
        }
        return tokenViewSet;
    }

    /**
     * @return user list
     */
    public Result getUserList() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        List<User> userList = userMapper.selectList(queryWrapper);
        List<UserWithTokenStatus> userTokenList = new ArrayList<UserWithTokenStatus>();
        for (User user : userList) {
            UserWithTokenStatus userWithTokenStatus = new UserWithTokenStatus();
            ClassUtil.covertFatherToChild(user, userWithTokenStatus);
            userWithTokenStatus.setActiveTokens(getTokenViews(user.getId().toString() + "_*"));
            userWithTokenStatus.setPassword("");
            userTokenList.add(userWithTokenStatus);
        }
        return Result.successResult(userTokenList);
    }

    /**
     * @param uuid
     * @return
     */
    public Result deleteUserToken(String uuid) {
        try {
            Set<String> keySet = redisUtil.keys("*_" + uuid);
            for (String key : keySet) {
                redisUtil.del(key);
            }
            return Result.successResult(MessageEnum.SUCCESS.getMsg());
        } catch (Exception e) {
            throw new InternalException(MessageEnum.ERROR.getMsg());
        }
    }
}
