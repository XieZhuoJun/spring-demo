package zhuojun.cruddemo.crud.common.annotation;

import zhuojun.cruddemo.crud.common.enums.RoleEnum;

import java.lang.annotation.*;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/05/29 10:41
 * @modified:
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AuthRequired {
    boolean required() default true;

    /**
     * 默认要求ADMIN角色访问
     * @return
     */
    RoleEnum role() default RoleEnum.ADMIN;

}
