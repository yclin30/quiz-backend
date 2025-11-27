package com.yclin.quiz.dto;

import lombok.Data;

@Data
public class UserLoginRequest {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String userPassword;
}