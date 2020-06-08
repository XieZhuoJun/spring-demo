package zhuojun.cruddemo.crud.common.exception;

import zhuojun.cruddemo.crud.common.enums.MessageEnum;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/06/07 22:02
 * @modified:
 */
public class NormalException extends BaseServerException{
    public NormalException() {
    }

    public NormalException(MessageEnum messageEnum) {
        super(messageEnum.getMsg());
    }

    public NormalException(String message, Object... args) {
        super(message, args);
    }

    public NormalException(Throwable cause) {
        super(cause);
    }
}
