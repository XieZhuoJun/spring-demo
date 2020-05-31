package zhuojun.cruddemo.crud.auth.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import zhuojun.cruddemo.crud.auth.domain.dto.LogForm;
import zhuojun.cruddemo.crud.common.constant.Constants;
import zhuojun.cruddemo.crud.common.util.JwtUtil;
import zhuojun.cruddemo.crud.system.domain.po.User;
import zhuojun.cruddemo.crud.auth.mapper.UserMapper;
import zhuojun.cruddemo.crud.common.base.domain.Result;
import zhuojun.cruddemo.crud.common.enums.MessageEnum;
import zhuojun.cruddemo.crud.common.exception.AuthenticationException;

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
        Generate token
         */
        Map<String, String> tokenMap = generateTokenMap(user.getId(), user.getPassword(), Constants.EXPIRE_TIME);
        return Result.successResult(MessageEnum.LOGIN_SUCCESS.getMsg(), tokenMap);
    }

    /**
     * @param token 旧的token
     * @return 包含新token的result
     */
    public Result refreshToken(String token) {

        User user = userMapper.selectById(JwtUtil.getJwtUserId(token));
        Map<String, String> tokenMap = generateTokenMap(user.getId(), user.getPassword(), Constants.EXPIRE_TIME);
        return Result.successResult(MessageEnum.LOGIN_SUCCESS.getMsg(), tokenMap);
    }

    private Map<String, String> generateTokenMap(Long userId, String password, Long expireTime) {
        String token = JwtUtil.generateJwt(userId.toString(), password, expireTime);
        Map<String, String> tokenMap = new HashMap<>(0);
        tokenMap.put(Constants.AUTH_HEADER_KEY, token);
        return tokenMap;
    }
}
