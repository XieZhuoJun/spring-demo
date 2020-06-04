package zhuojun.cruddemo.crud.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import zhuojun.cruddemo.crud.common.exception.RedisException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/06/04 16:23
 * @modified:
 */

@Slf4j
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @param key
     * @param time
     * @return
     */
    public Boolean expire(String key, long time) {
        try {
            return redisTemplate.expire(key, time, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Boolean.FALSE;
        }
    }

    /**
     * @param key
     * @return
     */
    public Long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RedisException(e);
        }
    }

    /**
     * @param key
     * @return
     */
    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RedisException(e);
        }
    }

    /**
     * @param key
     * @return
     */
    public Long del(String... key) {
        if (key != null && key.length > 0) {
            try {
                if (key.length == 1) {
                    Boolean code = redisTemplate.delete(key[0]);
                    if (code != null && code.equals(Boolean.TRUE)) {
                        return 1L;
                    } else {
                        return 0L;
                    }
                } else {
                    List<String> list = new ArrayList<String>(Arrays.asList(key));
                    return redisTemplate.delete(list);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new RedisException(e);
            }
        }
        return 0L;
    }

    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public Boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Boolean.FALSE;
        }
    }

    /**
     *
     * @param key
     * @param value
     * @param time
     * @return
     */
    public Boolean set(String key, Object value, Long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
                return true;
            } else {
                return set(key, value);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Boolean.FALSE;
        }
    }
}
