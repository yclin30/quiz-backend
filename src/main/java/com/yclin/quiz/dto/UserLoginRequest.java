package com.yclin.quiz.dto;

import lombok.Data;

/**
 * 用户登录请求
 */
@Data
public class UserLoginRequest {
    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPassword;
}
