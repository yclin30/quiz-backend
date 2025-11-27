package com.yclin.quiz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yclin.quiz.dto.UserLoginRequest;
import com.yclin.quiz.dto.UserRegisterRequest;
import com.yclin.quiz.model.domain.PageBean;
import com.yclin.quiz.model.domain.User;
import com.yclin.quiz.model.vo.UserLoginVO;


import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userRegisterRequest 用户注册请求
     * @return 新用户id
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 删除用户
     * @param id 用户id
     * @return 是否删除成功
     */
    public boolean deleteUserById(Long id);
    public boolean deleteUserByName(String username);

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return 用户信息
     */
    User getUserById(Long id);

    /**
     * 搜索用户
     * @param username 用户名
     * @return 用户列表
     */
    List<User> searchUsers(String username);
    /**
     * 硬删除 - 根据id
     */
    boolean hardDeleteById(Long id);

    /**
     * 硬删除 - 根据用户名
     */
    boolean hardDeleteByName(String username);

    PageBean page(Integer page, Integer pageSize);

    public List<User> findByName(String keyword);

    /**
     * 用户登录
     * @param loginRequest 登录请求
     * @return 登录响应（包含用户信息和Token）
     */
    UserLoginVO userLogin(UserLoginRequest loginRequest);
}