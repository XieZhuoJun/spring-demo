package zhuojun.cruddemo.crud.common.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zhuojun.cruddemo.crud.common.base.domain.Result;
import zhuojun.cruddemo.crud.common.exception.AuthenticationException;
import zhuojun.cruddemo.crud.common.exception.RegisterException;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/05/30 14:14
 * @modified:
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class MyExceptionHandler {

    private static final String ERROR = "错误";
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Object> exceptionHandler(Exception e){
        log.error(ERROR,e);
        return Result.failureResult(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public Result<Object> authenticationExceptionHandler(AuthenticationException e){
        log.warn(e.getMessage());
        return Result.failureResult(e.getMessage());
    }

    @ExceptionHandler(RegisterException.class)
    @ResponseBody
    public Result<Object> registerExceptionHandler(RegisterException e){
        log.warn(e.getMessage());
        return Result.failureResult(e.getMessage());
    }
}
