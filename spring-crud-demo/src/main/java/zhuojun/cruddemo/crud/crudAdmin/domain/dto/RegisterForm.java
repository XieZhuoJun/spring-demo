package zhuojun.cruddemo.crud.crudadmin.domain.dto;

import lombok.Data;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/05/30 13:38
 * @modified:
 */
@Data
public class RegisterForm {

    private String username;
    private String email;
    private String password;

    /**
     * Token to verify a register form
     */
    private String token;
}
