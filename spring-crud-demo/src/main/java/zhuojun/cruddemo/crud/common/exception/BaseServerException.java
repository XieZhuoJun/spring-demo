package zhuojun.cruddemo.crud.common.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/05/29 12:54
 * @modified:
 */
public class BaseServerException extends RuntimeException {
    /**
     * Error message
     */
    @Accessors(chain = true)
    @Setter
    @Getter
    private String errorMessage;

    /**
     * Message args
     */
    @Accessors(chain = true)
    @Setter
    @Getter
    private Object[] msgArgs;

    public BaseServerException() {
        super();
    }

    public BaseServerException(String message, Object... args) {
        super(String.format(message, args));
        this.msgArgs = args;
    }

    public BaseServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseServerException(Throwable cause, String message, Object... args) {
        super(String.format(message, args), cause);
    }

    public BaseServerException(Throwable cause) {
        super(cause);
    }
}
