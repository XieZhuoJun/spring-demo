package zhuojun.cruddemo.crud.common.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import zhuojun.cruddemo.crud.common.service.RedisService;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class RedisServiceImplTest {

    @Resource
    RedisService redisService;

    @Test
    void set() {
        redisService.set("Test", "test");
    }

    @Test
    void get() {
        System.out.println(redisService.get("Test"));
    }
}