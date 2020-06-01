package zhuojun.cruddemo.crud.common.service;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/06/01 11:32
 * @modified:
 */
public interface RedisService {
    boolean set(String key, String value);
    String get(String key);
    boolean expire(String key, long expire);
}
