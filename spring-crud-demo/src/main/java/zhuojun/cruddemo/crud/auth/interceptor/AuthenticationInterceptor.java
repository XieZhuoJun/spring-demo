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
import zhuojun.cruddemo.crud.common.constant.PlatformConstants;
import zhuojun.cruddemo.crud.common.domain.RedisTokenValue;
import zhuojun.cruddemo.crud.common.domain.User;
import zhuojun.cruddemo.crud.auth.mapper.AuthTokenMapper;
import zhuojun.cruddemo.crud.auth.mapper.UserMapper;
import zhuojun.cruddemo.crud.common.annotation.AuthRequired;
import zhuojun.cruddemo.crud.common.constant.Constants;
import zhuojun.cruddemo.crud.common.enums.MessageEnum;
import zhuojun.cruddemo.crud.common.enums.RoleEnum;
import zhuojun.cruddemo.crud.common.exception.AuthenticationException;
import zhuojun.cruddemo.crud.common.util.RedisUtil;

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
    private RedisUtil redisUtil;

    private User verifyJwtToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new AuthenticationException(MessageEnum.NO_AUTH);
        }

        /*
         * 验证流程：
         * decode获取用户id - 检查Expire time - 在Redis白名单中查询,获取token secret - 验证token
         */

        /*
         * 解析Token，获取用户id和过期时间
         */
        DecodedJWT jwt;
        try {
            jwt = JWT.decode(token);
        } catch (JWTDecodeException e) {
            //Invalid token, might have been edited
            throw new AuthenticationException(MessageEnum.INVALID_TOKEN);
        }

        /*
         * 提前检查是否过期，减少Redis操作
         */
        LocalDateTime expireDate = LocalDateTime.ofInstant(jwt.getExpiresAt().toInstant(), ZoneId.systemDefault());
        if (expireDate.isBefore(LocalDateTime.now())) {
            throw new AuthenticationException(MessageEnum.TOKEN_EXPIRED.getMsg());
        }

        /*
         * 在Redis白名单中查找token
         * key格式为：用户id + 平台id + token uuid
         */
        String key = jwt.getAudience().get(0) + "_" + jwt.getClaim(Constants.PLATFORM_CLAIM_KEY).asInt() + "_" + jwt.getClaim(Constants.UUID_CLAIM_KEY).asString();
        RedisTokenValue tokenValue = (RedisTokenValue) redisUtil.get(key);
        if (tokenValue == null || tokenValue.getSecret() == null || !tokenValue.getToken().equals(token)) {
            throw new AuthenticationException(MessageEnum.INVALID_TOKEN.getMsg());
        }

        /*
         Token验证
         */
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(tokenValue.getSecret())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new AuthenticationException(MessageEnum.INVALID_TOKEN.getMsg());
        }
        User user = new User();
        user.setId(Long.valueOf(jwt.getAudience().get(0)))
                .setRoleId(jwt.getClaims().get(Constants.ROLE_CLAIM_KEY).asInt());
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
             * 要求角色为游客的接口不需要权限，不用验证token
             */
            if (role == RoleEnum.VISITOR) {
                return true;
            }

            User user = verifyJwtToken(token);
            /*
             * 用户角色验证
             */
            if (RoleEnum.ADMIN.getRoleId().equals(user.getRoleId())) {
                //ADMIN拥有所有接口的权限
                return true;
            } else if (role == RoleEnum.USER && (RoleEnum.USER.getRoleId().equals(user.getRoleId()))) {
                //USER权限验证
                return true;
            } else {
                throw new AuthenticationException(MessageEnum.NO_AUTH);
            }
        }
        return true;
    }
}
