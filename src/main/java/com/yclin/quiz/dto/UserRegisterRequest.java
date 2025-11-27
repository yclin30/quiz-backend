package com. yclin.quiz.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String userPassword;

    private String checkPassword;

    /**
     * 🆕 用户角色：0-普通用户，1-管理员
     */
    private Integer userRole;
}