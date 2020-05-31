package zhuojun.cruddemo.crud.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/05/30 19:56
 * @modified:
 */
public class JwtUtil {

    /**
     * @param id        持有用户id
     * @param secret    用户密码作为加密密钥
     * @param ttlMillis 有效期，单位毫秒
     * @return JWT token
     */
    public static String generateJwt(String id, String secret, long ttlMillis) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withAudience(id)
                .withExpiresAt(new Date(System.currentTimeMillis() + ttlMillis))
                .sign(algorithm);
    }

    public static String getJwtUserId(String token) {
        return JWT.decode(token).getAudience().get(0);
    }
}

