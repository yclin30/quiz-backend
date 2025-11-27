package com.yclin.quiz.config;

import com.yclin.quiz.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置类
 * 注册拦截器并配置拦截规则
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")          // 拦截所有请求
                .excludePathPatterns(            // 放行的路径
                        "/user/login",           // 登录接口
                        "/user/register",        // 注册接口
                        "/error"                 // 错误页面
                );
    }
}
