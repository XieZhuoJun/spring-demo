package zhuojun.cruddemo.crud.auth.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import zhuojun.cruddemo.crud.system.domain.po.User;
import zhuojun.cruddemo.crud.auth.mapper.AuthTokenMapper;
import zhuojun.cruddemo.crud.auth.mapper.UserMapper;
import zhuojun.cruddemo.crud.common.annotation.AuthRequired;
import zhuojun.cruddemo.crud.common.constant.Constants;
import zhuojun.cruddemo.crud.common.enums.MessageEnum;
import zhuojun.cruddemo.crud.common.enums.RoleEnum;
import zhuojun.cruddemo.crud.common.exception.AuthenticationException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/05/29 12:27
 * @modified:
 */
@Slf4j
@Configuration
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Resource
    private AuthTokenMapper authTokenMapper;

    @Resource
    private UserMapper userMapper;

    private User verifyJwtToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new AuthenticationException(MessageEnum.NO_AUTH);
        }

        /*
         * 验证流程：
         * Decode获取用户id - 检查Expire time - 从用户表中查到密码，作为密钥验证token - 在Redis白名单中查询
         */

        /*
         * 解析Token，获取用户id和过期时间
         */
        DecodedJWT jwt;
        try {
            jwt = JWT.decode(token);
        } catch (JWTDecodeException e) {
            //Invalid token
            throw new AuthenticationException(MessageEnum.INVALID_TOKEN);
        }

        /*
         * 提前检查是否过期，减少数据库操作
         */
        LocalDateTime expireDate = LocalDateTime.ofInstant(jwt.getExpiresAt().toInstant(), ZoneId.systemDefault());
        if (expireDate.isBefore(LocalDateTime.now())) {
            throw new AuthenticationException(MessageEnum.TOKEN_EXPIRED);
        }

        /*
         * 通过id，从数据库中获取用户对象
         */
        User user = userMapper.selectById(jwt.getAudience().get(0));
        if (user == null) {
            throw new AuthenticationException(MessageEnum.INVALID_TOKEN);
        }

        /*
         Token验证
         */
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new AuthenticationException(MessageEnum.INVALID_TOKEN);
        }

        /*
         * 在Redis白名单中查找token
         */
        Map<String, Object> selectMap = new HashMap<>(0);
        selectMap.put("token", token);
        if (authTokenMapper.selectByMap(selectMap) == null) {
            throw new AuthenticationException(MessageEnum.INVALID_TOKEN);
        }

        return user;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws ServletException {
        String token = httpServletRequest.getHeader(Constants.AUTH_HEADER_KEY);
        /*
          检查是否为HandlerMethod，不是则直接跳过
         */
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        /*
          不带@AuthRequired注解的方法无需检查
         */
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (!method.isAnnotationPresent(AuthRequired.class)) {
            return true;
        }

        log.debug(method.getName());
        /*
         * 获取权限要求
         */
        AuthRequired authRequired = (AuthRequired) method.getAnnotation(AuthRequired.class);

        /*
         * 判断是否有权限
         */
        if (authRequired.required()) {
            RoleEnum role = authRequired.role();

            /*
             * 游客不需要权限，Token为空也可访问
             */
            if (role == RoleEnum.VISITOR) {
                return true;
            }

            User user = verifyJwtToken(token);
            /*
             * 用户角色验证
             */
            if (role == RoleEnum.ADMIN && !RoleEnum.ADMIN.getRoleId().equals(user.getRoleId())) {
                throw new AuthenticationException(MessageEnum.NO_AUTH);
            }

            if (role == RoleEnum.USER && !(RoleEnum.USER.getRoleId().equals(user.getRoleId()))) {
                throw new AuthenticationException(MessageEnum.NO_AUTH);
            }
        }
        return true;
    }
}
