package zhuojun.cruddemo.crud.common.base.domain;

import lombok.Data;
import zhuojun.cruddemo.crud.common.constant.Constants;

import java.io.Serializable;

/**
 * @author zhuojun
 * @date 2020-05-28 20:50
 **/
@Data
public class Result<T> implements Serializable {
    /**
     * For RPC use
     */
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> successResult(){
        Result<T> result = new Result<T>();
        result.code = Constants.SUCCESS;
        return result;
    }

    public static <T> Result<T> successResult(T data) {
        Result<T> result = new Result<T>();
        result.code = Constants.SUCCESS;
        result.data = data;
        return result;
    }

    public static <T> Result<T> successResult(String message){
        Result<T> result = new Result<T>();
        result.code = Constants.SUCCESS;
        result.message = message;
        return result;
    }

    public static <T> Result<T> successResult(String message, T data){
        Result<T> result = new Result<T>();
        result.code = Constants.SUCCESS;
        result.message = message;
        result.data = data;
        return result;
    }

    public static <T> Result<T> failureResult(){
        Result<T> result = new Result<T>();
        result.code = Constants.FAILURE;
        return result;
    }

    public static <T> Result<T> failureResult(T data) {
        Result<T> result = new Result<T>();
        result.code = Constants.FAILURE;
        result.data = data;
        return result;
    }

    public static <T> Result<T> failureResult(String message){
        Result<T> result = new Result<T>();
        result.code = Constants.FAILURE;
        result.message = message;
        return result;
    }

    public static <T> Result<T> failureResult(String message, T data){
        Result<T> result = new Result<T>();
        result.code = Constants.FAILURE;
        result.message = message;
        result.data = data;
        return result;
    }
}
