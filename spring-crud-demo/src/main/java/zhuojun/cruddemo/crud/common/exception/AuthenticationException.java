package zhuojun.cruddemo.crud.common.exception;

import zhuojun.cruddemo.crud.common.enums.MessageEnum;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/05/29 12:54
 * @modified:
 */
public class AuthenticationException extends BaseServerException {

    public AuthenticationException() {
    }

    public AuthenticationException(MessageEnum messageEnum){
        super(messageEnum.getMsg());
    }
    public AuthenticationException(String message, Object... args) {
        super(message, args);
    }
}
