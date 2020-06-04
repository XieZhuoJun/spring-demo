package zhuojun.cruddemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import zhuojun.cruddemo.crud.auth.domain.po.AuthToken;
import zhuojun.cruddemo.crud.auth.mapper.AuthTokenMapper;
import zhuojun.cruddemo.crud.common.util.RedisUtil;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/05/29 20:33
 * @modified:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {

    @Resource
    private AuthTokenMapper authTokenMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void insert() {
        AuthToken token = new AuthToken();
        token.setToken("Test")
                .setExpireTime(LocalDateTime.now())
                .setUserId(1L);
        Integer result = authTokenMapper.insert(token);
        authTokenMapper.insert(token);
        System.out.println(result);
        System.out.println(authTokenMapper.selectById(1L));

    }
}
