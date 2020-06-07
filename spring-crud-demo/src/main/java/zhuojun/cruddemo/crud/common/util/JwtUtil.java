package zhuojun.cruddemo.crud.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import zhuojun.cruddemo.crud.common.constant.Constants;
import zhuojun.cruddemo.crud.common.domain.JwtParam;

import java.util.Date;
import java.util.UUID;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/05/30 19:56
 * @modified:
 */

public class JwtUtil<T> {

    /**
     * @param id        持有用户id
     * @param secret    用户密码作为加密密钥
     * @param ttlMillis 有效期，单位毫秒
     * @return JWT token
     */
    public static String generateJwt(String id, String secret, Integer roleId,Integer platformId,  long ttlMillis) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withAudience(id)
                .withExpiresAt(new Date(System.currentTimeMillis() + ttlMillis))
                .withClaim(Constants.ROLE_CLAIM_KEY, roleId)
                .withClaim(Constants.PLATFORM_CLAIM_KEY,platformId)
                .withClaim(Constants.UUID_CLAIM_KEY, UUID.randomUUID().toString().replace("-", ""))
                .sign(algorithm);
    }

    public static String generateJwt(JwtParam jwtparam) {
        return generateJwt(jwtparam.getId().toString(), jwtparam.getSecret(), jwtparam.getRoleId(),jwtparam.getPlatform(), jwtparam.getExpireTime());
    }

    public static String getJwtUserId(String token) {
        return JWT.decode(token).getAudience().get(0);
    }
}

