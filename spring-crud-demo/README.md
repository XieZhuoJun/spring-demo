# spring-crud-demo
Spring Boot 学习 - 用户注册、登录、权限管理、用户管理简单实现

## 技术栈
* 数据库操作：Mybatis-plus
* 登录验证&密码管理：BCrypt
* 接口鉴权：自定义注解 + 拦截器 + JWT + Redis白名单验证(TODO)
* 登录Token管理：Redis白名单 + 用户密码作为JWT密钥，用户密码更改后Token自动失效