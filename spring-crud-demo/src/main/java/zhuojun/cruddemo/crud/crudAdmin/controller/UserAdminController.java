package zhuojun.cruddemo.crud.crudAdmin.controller;

import org.springframework.web.bind.annotation.*;
import zhuojun.cruddemo.crud.common.annotation.AuthRequired;
import zhuojun.cruddemo.crud.common.domain.Result;
import zhuojun.cruddemo.crud.common.enums.MessageEnum;
import zhuojun.cruddemo.crud.common.enums.RoleEnum;
import zhuojun.cruddemo.crud.common.exception.AuthenticationException;
import zhuojun.cruddemo.crud.common.util.FormatValidate;
import zhuojun.cruddemo.crud.crudAdmin.domain.dto.RegisterForm;
import zhuojun.cruddemo.crud.crudAdmin.service.UserAdminService;

import javax.annotation.Resource;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/05/30 13:36
 * @modified:
 */
@RestController
@RequestMapping("/v1/user")
public class UserAdminController {
    @Resource
    UserAdminService userAdminService;

    @PostMapping("/register")
    public Result userRegister(@RequestBody RegisterForm registrationForm) {
        /*
        去除密码首尾空格
         */
        registrationForm.setPassword(registrationForm.getPassword().trim());

        /*
        验证密码格式
        密码格式验证 + 邮箱格式验证 + 用户名长度验证
         */
        boolean valid = FormatValidate.verifyPassword(registrationForm.getPassword())
                && (registrationForm.getEmail() != null && registrationForm.getUsername() != null);
        if (!valid) {
            throw new AuthenticationException(MessageEnum.IVALID_PARAM);
        }

        return userAdminService.register(registrationForm);
    }

    @AuthRequired(role = RoleEnum.ADMIN)
    @GetMapping("/list")
    public Result fetchUserList(){
        return userAdminService.getUserList();
    }


}
