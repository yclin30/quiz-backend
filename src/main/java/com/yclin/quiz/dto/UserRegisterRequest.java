package com.yclin.quiz.dto;

import lombok.Data;

@Data
public class UserRegisterRequest {
    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 校验密码
     */
    private String checkPassword;
}