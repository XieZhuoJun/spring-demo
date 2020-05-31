package zhuojun.cruddemo.crud.auth.controller;

import org.springframework.web.bind.annotation.*;
import zhuojun.cruddemo.crud.auth.domain.dto.LogForm;
import zhuojun.cruddemo.crud.auth.service.AuthenticationService;
import zhuojun.cruddemo.crud.common.annotation.AuthRequired;
import zhuojun.cruddemo.crud.common.base.domain.Result;
import zhuojun.cruddemo.crud.common.constant.Constants;
import zhuojun.cruddemo.crud.common.enums.MessageEnum;
import zhuojun.cruddemo.crud.common.enums.RoleEnum;
import zhuojun.cruddemo.crud.common.exception.AuthenticationException;
import zhuojun.cruddemo.crud.common.util.FormatValidate;

import javax.annotation.Resource;

/**
 * @author zhuojun
 * @date 2020-05-28 20:50
 **/
@RestController
@RequestMapping("/v1/auth")
public class AuthUserController {
    @Resource
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public Result userLogin(@RequestBody LogForm logForm) {
        /*
        去除密码首尾空格
         */
        logForm.setPassword(logForm.getPassword().trim());

        /*
        验证密码格式
         */
        boolean valid = FormatValidate.verifyPassword(logForm.getPassword())
                && (logForm.getEmail() != null || logForm.getUsername() != null);

        if (!valid) {
            throw new AuthenticationException(MessageEnum.IVALID_PARAM);
        }
        return authenticationService.authLogInfo(logForm);
    }

    @AuthRequired(role = RoleEnum.USER)
    @GetMapping("/refreshToken")
    public Result refreshToken(@RequestHeader(Constants.AUTH_HEADER_KEY) String token){
        return authenticationService.refreshToken(token);
    }
}
