package zhuojun.cruddemo.crud.crudAdmin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import zhuojun.cruddemo.crud.auth.mapper.UserMapper;
import zhuojun.cruddemo.crud.common.base.domain.Result;
import zhuojun.cruddemo.crud.common.enums.MessageEnum;
import zhuojun.cruddemo.crud.common.enums.RoleEnum;
import zhuojun.cruddemo.crud.common.exception.AuthenticationException;
import zhuojun.cruddemo.crud.common.exception.RegisterException;
import zhuojun.cruddemo.crud.crudAdmin.domain.dto.RegisterForm;
import zhuojun.cruddemo.crud.system.domain.po.User;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/05/30 13:36
 * @modified:
 */
@Slf4j
@Service
public class UserAdminService {
    @Resource
    private UserMapper userMapper;

    public Result register(RegisterForm registerForm) {
        User newUser = new User();
        newUser.setEmail(registerForm.getEmail())
                .setUsername(registerForm.getUsername())
                .setRoleId(RoleEnum.USER.getRoleId())
                .setCreateTime(LocalDateTime.now());

        /*
        检查用户是否已存在
         */
        Map<String, Object> selectMap = new HashMap<>(0);
        selectMap.put("username", newUser.getUsername());
        if (!userMapper.selectByMap(selectMap).isEmpty()) {
            throw new RegisterException(MessageEnum.ACCOUNT_EXIST.getMsg());
        }
        selectMap.clear();
        selectMap.put("email", newUser.getEmail());
        if (!userMapper.selectByMap(selectMap).isEmpty()) {
            throw new RegisterException(MessageEnum.ACCOUNT_EXIST.getMsg());
        }

        /*
        使用BCrypt加密密码后放入数据库
         */
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        newUser.setPassword(passwordEncoder.encode(registerForm.getPassword()));
        if (userMapper.insert(newUser) == 0) {
            throw new AuthenticationException(MessageEnum.ERROR.getMsg());
        } else {
            return Result.successResult(MessageEnum.SUCCESS.getMsg());
        }
    }

    public Result getUserList() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        List<User> userList = userMapper.selectList(queryWrapper);
        return Result.successResult(userList);

    }
}
