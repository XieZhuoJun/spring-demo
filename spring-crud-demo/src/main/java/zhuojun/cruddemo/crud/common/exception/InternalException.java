package zhuojun.cruddemo.crud.common.exception;

import zhuojun.cruddemo.crud.common.enums.MessageEnum;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/06/07 16:46
 * @modified:
 */
public class InternalException extends BaseServerException {
    public InternalException() {
    }

    public InternalException(MessageEnum messageEnum) {
        super(messageEnum.getMsg());
    }

    public InternalException(String message, Object... args) {
        super(message, args);
    }

    public InternalException(Throwable cause) {
        super(cause);
    }
}
