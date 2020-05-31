package zhuojun.cruddemo.crud.auth.domain.dto;

import lombok.Data;

/**
 * @author: zhuojun
 * @description: 用户登录信息类
 * @date: 2020/05/29 18:56
 * @modified:
 */
@Data
public class LogForm {
    private String username;
    private String email;
    private String password;

    /**
     * Token to verify a login form
     */
    private String token;
}
