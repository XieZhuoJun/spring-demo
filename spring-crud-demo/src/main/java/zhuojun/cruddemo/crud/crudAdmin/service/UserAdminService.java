package zhuojun.cruddemo.crud.crudAdmin.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import zhuojun.cruddemo.crud.auth.mapper.UserMapper;
import zhuojun.cruddemo.crud.common.constant.Constants;
import zhuojun.cruddemo.crud.common.domain.Result;
import zhuojun.cruddemo.crud.common.domain.TokenView;
import zhuojun.cruddemo.crud.common.domain.UserWithTokenStatus;
import zhuojun.cruddemo.crud.common.enums.MessageEnum;
import zhuojun.cruddemo.crud.common.enums.RoleEnum;
import zhuojun.cruddemo.crud.common.exception.AuthenticationException;
import zhuojun.cruddemo.crud.common.exception.RegisterException;
import zhuojun.cruddemo.crud.common.util.ClassUtil;
import zhuojun.cruddemo.crud.common.util.RedisUtil;
import zhuojun.cruddemo.crud.crudAdmin.domain.dto.RegisterForm;
import zhuojun.cruddemo.crud.common.domain.User;

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

    private Set<TokenView> getTokenViews(String pattern) {
        Set<String> keySet = redisUtil.keys(pattern);
        Set<TokenView> tokenViewSet = new HashSet<>();
        if(keySet != null){
            for(String key : keySet){
                try{
                    TokenView tokenView = new TokenView();
                    String token = (String)redisUtil.get(key);
                    DecodedJWT jwt = JWT.decode(token);
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

    public Result getUserList() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        List<User> userList = userMapper.selectList(queryWrapper);
        List<UserWithTokenStatus> userTokenList = new ArrayList<UserWithTokenStatus>();
        for(User user : userList){
            UserWithTokenStatus userWithTokenStatus = new UserWithTokenStatus();
            ClassUtil.covertFatherToChild(user,userWithTokenStatus);
            userWithTokenStatus.setAvailableTokens(getTokenViews(user.getId().toString() + "_*"));
            userWithTokenStatus.setPassword("");
            userTokenList.add(userWithTokenStatus);
        }
        return Result.successResult(userTokenList);
    }

    public Result deleteUserToken(Long userId, Integer platformId){

        return Result.successResult(MessageEnum.SUCCESS.getMsg());
    }
}
