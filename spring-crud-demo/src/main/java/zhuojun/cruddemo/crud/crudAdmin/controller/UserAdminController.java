package zhuojun.cruddemo.crud.crudadmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zhuojun.cruddemo.crud.common.annotation.AuthRequired;
import zhuojun.cruddemo.crud.common.domain.Result;
import zhuojun.cruddemo.crud.common.enums.MessageEnum;
import zhuojun.cruddemo.crud.common.enums.RoleEnum;
import zhuojun.cruddemo.crud.common.exception.AuthenticationException;
import zhuojun.cruddemo.crud.common.exception.NormalException;
import zhuojun.cruddemo.crud.common.util.FormatValidate;
import zhuojun.cruddemo.crud.crudadmin.domain.dto.RegisterForm;
import zhuojun.cruddemo.crud.crudadmin.service.UserAdminService;

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
    /**
     *
     * @param registrationForm
     * @return
     */
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

    /**
     * 返回用户列表，包含用户状态信息
     * @return
     */
    @AuthRequired(role = RoleEnum.ADMIN)
    @GetMapping("/list")
    public Result fetchUserList(){
        return userAdminService.getUserList();
    }


    /**
     * @TODO 验证uuid格式，防止正则表达式注入
     * @param uuid
     * @return
     */
    @AuthRequired(role = RoleEnum.ADMIN)
    @DeleteMapping("/token/{uuid}")
    public Result deleteToken(@PathVariable("uuid") String uuid){
        if(!FormatValidate.verifyUUID(uuid)){
            throw new NormalException(MessageEnum.IVALID_PARAM.getMsg());
        }
        return userAdminService.deleteUserToken(uuid);
    }
}