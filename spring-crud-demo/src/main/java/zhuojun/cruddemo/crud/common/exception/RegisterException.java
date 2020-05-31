package zhuojun.cruddemo.crud.common.exception;

import zhuojun.cruddemo.crud.common.enums.MessageEnum;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/05/30 13:46
 * @modified:
 */
public class RegisterException extends BaseServerException {
    public RegisterException() {
    }

    public RegisterException(MessageEnum messageEnum){
        super(messageEnum.getMsg());
    }
    public RegisterException(String message, Object... args) {
        super(message, args);
    }
}
