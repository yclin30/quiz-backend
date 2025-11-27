package com. yclin.quiz.config;

import com. yclin.quiz.interceptor. AdminInterceptor;
import com.yclin.quiz.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet. config.annotation.WebMvcConfigurer;

/**
 * Web 配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 🔧 注意：拦截器的顺序很重要，先验证 JWT，再验证管理员权限

        // JWT Token 验证拦截器（所有需要登录的接口）
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/hello",
                        "/simpleParam",
                        "/error"
                );

        // 管理员接口拦截器（需要管理员权限）
        registry.addInterceptor(adminInterceptor)
                . addPathPatterns(
                        "/user/add",              // 添加用户
                        "/user/update",           // 🆕 更新用户
                        "/user/resetPassword/**", // 重置密码
                        "/user/users",            // 用户列表
                        "/user/search",           // 搜索用户
                        "/user/hard/**",          // 硬删除用户
                        "/user/name/**",          // 按用户名删除
                        "/user/*/",               // DELETE 用户
                        "/question/**"            // 所有题目管理接口
                )
                .excludePathPatterns(
                        "/user/login",
                        "/user/register"
                );
    }

    /**
     * 🔧 配置跨域 - 确保支持 PUT 方法
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                . allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                . maxAge(3600);
    }
}