package zhuojun.cruddemo.crud.common.exception;

import zhuojun.cruddemo.crud.common.enums.MessageEnum;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/06/04 17:13
 * @modified:
 */
public class RedisException extends BaseServerException {

    public RedisException() {
    }

    public RedisException(MessageEnum messageEnum) {
        super(messageEnum.getMsg());
    }

    public RedisException(String message, Object... args) {
        super(message, args);
    }

    public RedisException(Throwable cause) {
        super(cause);
    }
}