package zhuojun.cruddemo.crud.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import zhuojun.cruddemo.crud.auth.domain.dto.LogForm;
import zhuojun.cruddemo.crud.common.constant.Constants;
import zhuojun.cruddemo.crud.common.constant.PlatformConstants;
import zhuojun.cruddemo.crud.common.domain.JwtParam;
import zhuojun.cruddemo.crud.common.domain.RedisTokenValue;
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

    @Resource
    private RedisUtil redisUtil;

    /**
     * @param logForm 登录表单
     * @return 带有token的Result
     * @description Verify user login info and response with token
     */
    public Result verifyLogInfo(LogForm logForm) {
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
        DecodedJWT jwt = JWT.decode(tokenMap.get(Constants.AUTH_HEADER_KEY));
        String token = tokenMap.get(Constants.AUTH_HEADER_KEY);
        String secret = user.getPassword();
        Long userId = user.getId();
        Integer platformId = jwt.getClaim(Constants.PLATFORM_CLAIM_KEY).asInt();
        String uuid = jwt.getClaim(Constants.UUID_CLAIM_KEY).asString();

        if(!addTokenToRedis(token,secret,userId,platformId,uuid)){
            throw new AuthenticationException(MessageEnum.ERROR);
        }
        return Result.successResult(MessageEnum.LOGIN_SUCCESS.getMsg(), tokenMap);
    }

    /**
     * @param token 旧的token
     * @return 包含新token的result
     */
    public Result refreshToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        String key = jwt.getAudience().get(0) + "_" + jwt.getClaim(Constants.PLATFORM_CLAIM_KEY).asInt() + "_" + jwt.getClaim(Constants.UUID_CLAIM_KEY).asString();
        RedisTokenValue tokenValue = (RedisTokenValue)redisUtil.get(key);
        if (tokenValue == null || tokenValue.getSecret() == null || !tokenValue.getToken().equals(token)) {
            throw new AuthenticationException(MessageEnum.INVALID_TOKEN.getMsg());
        }
        String newToken = JwtUtil.refreshToken(token,tokenValue.getSecret(),Constants.EXPIRE_TIME);
        Map<String, String> tokenMap = new HashMap<>(0);
        tokenMap.put(Constants.AUTH_HEADER_KEY, newToken);
        return Result.successResult(MessageEnum.LOGIN_SUCCESS.getMsg(), tokenMap);
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

    private Boolean addTokenToRedis(String token, String secret, Long userId, Integer platformId, String uuid) {
        String key = userId.toString() + "_" + platformId.toString() + "_" + uuid;
        RedisTokenValue redisTokenValue = new RedisTokenValue();
        redisTokenValue.setToken(token)
                .setSecret(secret);
        return redisUtil.set(key, redisTokenValue, Constants.EXPIRE_TIME / 1000);
    }
}
