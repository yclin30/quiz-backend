package com.yclin.quiz.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField(value = "userName")
    private String userName;

    /**
     * 密码
     */
    @TableField(value = "userPassword")
    private String userPassword;

    /**
     *
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    /**
     *
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     *
     */
    @TableLogic
    @TableField(value = "isDelete")
    private Integer isDelete;

    /**
     * 表示用户角色， 0 普通用户， 1 管理员
     */
    @TableField(value = "userRole")
    private Integer userRole;
}