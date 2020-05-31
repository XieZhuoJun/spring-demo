package zhuojun.cruddemo.crud.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import zhuojun.cruddemo.crud.auth.interceptor.AuthenticationInterceptor;

import javax.annotation.Resource;

/**
 * @author: zhuojun
 * @description:
 * @date: 2020/05/31 12:39
 * @modified:
 */

@Configuration
public class WebConverterConfigurer implements WebMvcConfigurer {

    @Resource
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor);
    }

}
