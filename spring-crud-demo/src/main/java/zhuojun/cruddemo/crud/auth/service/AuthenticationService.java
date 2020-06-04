package zhuojun.cruddemo.crud.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import zhuojun.cruddemo.crud.auth.domain.dto.LogForm;
import zhuojun.cruddemo.crud.common.constant.Constants;
import zhuojun.cruddemo.crud.common.constant.PlatformConstants;
import zhuojun.cruddemo.crud.common.domain.JwtParam;
import zhuojun.cruddemo.crud.common.util.JwtUtil;
import zhuojun.cruddemo.crud.common.domain.User;
import zhuojun.cruddemo.crud.auth.mapper.UserMapper;
import zhuojun.cruddemo.crud.common.domain.Result;
import zhuojun.cruddemo.crud.common.enums.MessageEnum;
import zhuojun.cruddemo.crud.common.exception.AuthenticationException;
import zhuojun.cruddemo.crud.common.util.RedisUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/05/29 18:26
 * @modified:
 */
@Service
public class AuthenticationService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisutil;

    /**
     * @param logForm 登录表单
     * @return 带有token的Result
     * @description Verify user loginfo and response with token
     */
    public Result authLogInfo(LogForm logForm) {
        /*
         Format Validating should be done at controller
         */

        /*
         Query User in db
         */
        Map<String, Object> selectMap = new HashMap<>(0);
        if (logForm.getEmail() != null) {
            selectMap.put("email", logForm.getEmail());
        } else {
            selectMap.put("username", logForm.getUsername());
        }
        User user = userMapper.selectByMap(selectMap).get(0);
        if (user == null) {
            throw new AuthenticationException(MessageEnum.LOGIN_ERROR);
        }

        /*
         Verify Password
         */
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(logForm.getPassword(), user.getPassword())) {
            throw new AuthenticationException(MessageEnum.LOGIN_ERROR);
        }

        /*
        Generate token and add to redis
         */
        Map<String, String> tokenMap = generateTokenMap(user);
        if(!addTokenToRedis(tokenMap.get(Constants.AUTH_HEADER_KEY), user.getId(),PlatformConstants.DESKTOP_BROWSER,user.getPassword())){
            throw new AuthenticationException(MessageEnum.ERROR);
        }
        return Result.successResult(MessageEnum.LOGIN_SUCCESS.getMsg(), tokenMap);
    }

    /**@TODO
     * @param token 旧的token
     * @return 包含新token的result
     */
    public Result refreshToken(String token) {
        return new Result();
    }

    private Map<String, String> generateTokenMap(User user) {
        JwtParam jwtParam = new JwtParam();
        jwtParam.setId(user.getId())
                .setRoleId(user.getRoleId())
                .setExpireTime(Constants.EXPIRE_TIME)
                .setSecret(user.getPassword())
                .setPlatform(PlatformConstants.DESKTOP_BROWSER);
        String token = JwtUtil.generateJwt(jwtParam);
        Map<String, String> tokenMap = new HashMap<>(0);
        tokenMap.put(Constants.AUTH_HEADER_KEY, token);
        return tokenMap;
    }

    private Boolean addTokenToRedis(String token,Long userId, Integer platform, String secret){
        String key = userId.toString() + "_" + platform.toString() + "_" + token;
        return redisutil.set(key,secret,Constants.EXPIRE_TIME / 1000);
    }
}
