package com.yclin.quiz.model.vo;

import lombok.Data;

/**
 * 用户登录响应
 */
@Data
public class UserLoginVO {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户角色
     */
    private Integer userRole;

    /**
     * JWT Token
     */
    private String token;
}
