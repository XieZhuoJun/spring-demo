package zhuojun.cruddemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import zhuojun.cruddemo.crud.common.domain.TokenView;
import zhuojun.cruddemo.crud.auth.mapper.AuthTokenMapper;

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
    }
}
