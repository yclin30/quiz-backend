package com.yclin.quiz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yclin.quiz.dto.UserLoginRequest;
import com.yclin.quiz.dto.UserRegisterRequest;
import com.yclin.quiz.mapper.UserMapper;
import com.yclin.quiz.model.domain.PageBean;
import com.yclin.quiz.model.domain.User;

import com.yclin.quiz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private UserMapper userMapper;


    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "yclin";

    @Override
    public long userRegister(UserRegisterRequest userRegisterRequest) {
        String userName = userRegisterRequest.getUserName();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        // 1. 校验参数是否为空
        if (StringUtils.isAnyBlank(userName, userPassword, checkPassword)) {
            throw new RuntimeException("参数为空");
        }

        // 2. 校验用户名长度
        if (userName.length() < 4) {
            throw new RuntimeException("用户名长度不能小于4位");
        }

        // 3. 校验密码长度
        if (userPassword.length() < 8) {
            throw new RuntimeException("密码长度不能小于8位");
        }

        // 4. 校验密码和确认密码是否一致
        if (!userPassword.equals(checkPassword)) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        // 5. 校验用户名是否包含特殊字符
        String validPattern = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userName);
        if (!matcher.find()) {
            throw new RuntimeException("用户名不能包含特殊字符");
        }

        // 6. 校验用户名是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName", userName);
        long count = this.count(queryWrapper);
        if (count > 0) {


            throw new RuntimeException("用户名已存在");
        }

        // 7. 对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex(
                (SALT + userPassword).getBytes());

        // 8. 创建用户对象
        User user = new User();
        user.setUserName(userName);
        user.setUserPassword(encryptPassword);
        user.setUserRole(0); // 默认为普通用户


        // 9. 保存用户
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new RuntimeException("注册失败");
        }

        return user.getId();
    }

    // 在 UserServiceImpl 类中添加以下方法

    @Override
    public User userLogin(UserLoginRequest userLoginRequest) {
        String userName = userLoginRequest.getUserName();
        String userPassword = userLoginRequest.getUserPassword();

        // 1. 校验参数
        if (StringUtils.isAnyBlank(userName, userPassword)) {
            throw new RuntimeException("用户名或密码不能为空");
        }

        // 2. 查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName", userName);
        queryWrapper.eq("isDelete", 0);
        User user = this.getOne(queryWrapper);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 3. 验证密码
        String encryptPassword = DigestUtils.md5DigestAsHex(
                (SALT + userPassword).getBytes());

        if (!encryptPassword.equals(user.getUserPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 4. 返回用户信息（脱敏）
        return getSafetyUser(user);
    }

    @Override
    @Transactional
    public boolean deleteUserById(Long id) {
        if (id == null || id <= 0) {
            throw new RuntimeException("用户id不合法");
        }
        // 先检查用户是否存在
        User user = this.getById(id);
        if (user == null || user.getIsDelete() == 1) {
            throw new RuntimeException("用户不存在");
        }

        // 调用mapper的删除方法
        int rows = userMapper.deleteUserById(id);
        log.info("删除用户，影响行数：{}", rows);
        return rows > 0;
    }

    @Override
    @Transactional
    public boolean deleteUserByName(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }

        // 调用mapper的删除方法
        int rows = userMapper.deleteByUsername(username);
        log.info("删除用户，影响行数：{}", rows);
        return rows > 0;
    }

    @Override
    @Transactional
    public boolean hardDeleteById(Long id) {
        if (id == null || id <= 0) {
            throw new RuntimeException("用户id不合法");
        }

        // 检查用户是否存在
        User user = this.getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        int rows = userMapper.hardDeleteById(id);
        log.info("硬删除用户，id = {}，影响行数：{}", id, rows);
        return rows > 0;
    }

    @Override
    @Transactional
    public boolean hardDeleteByName(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }

        // 检查用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName", username);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        int rows = userMapper.hardDeleteByUsername(username);
        log.info("硬删除用户，username = {}，影响行数：{}", username, rows);
        return rows > 0;
    }
    @Override
    public User getUserById(Long id) {
        if (id == null || id <= 0) {
            throw new RuntimeException("用户id不合法");
        }
        User user = this.getById(id);
        if (user == null || user.getIsDelete() == 1) {
            throw new RuntimeException("用户不存在");
        }
        // 脱敏处理
        return getSafetyUser(user);
    }

    @Override
    public List<User> searchUsers(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (username != null && !username.trim().isEmpty()) {
            queryWrapper.like("userName", username);
        }
        // 只查询未删除的用户
        queryWrapper.eq("isDelete", 0);
        List<User> userList = this.list(queryWrapper);
        // 脱敏处理
        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
    }

    /**
     * 用户脱敏
     * @param originUser 原始用户
     * @return 脱敏后的用户
     */
    private User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUserName(originUser.getUserName());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setCreateTime(originUser.getCreateTime());
        // 不返回敏感信息
        safetyUser.setUserPassword(null);
        return safetyUser;
    }

    public PageBean page(Integer page, Integer pageSize){
        //获取总的记录数；
        Integer total=userMapper.count();

        //获取分页查询结果列表；
        Integer start = (page-1)*pageSize;
        List<User> userList=userMapper.page(start, pageSize);

        //封装PageBean对象；
        PageBean pageBean = new PageBean();
        pageBean.setTotal(total);
        pageBean.setRows(userList);

        return pageBean;
    }

    public List<User> findByName(String keyword){
        List<User> userList=userMapper.findByName(keyword);
        for(User user:userList){
            user.setUserPassword("*******");
        }
        return userList;
    }


    /**
     * 🆕 重置用户密码为 "123456"
     */
    @Override
    public boolean resetPassword(Long id) {
        // 1. 检查用户是否存在
        User user = userMapper.selectById(id);
        if (user == null || user.getIsDelete() == 1) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 加密新密码 "123456"
        String newPassword = "123456";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + newPassword).getBytes());

        // 3. 更新密码
        user.setUserPassword(encryptPassword);
        user.setUpdateTime(new Date());

        int rows = userMapper.updateById(user);
        return rows > 0;
    }

    /**
     * 🆕 管理员添加用户（可选择角色）
     */
    @Override
    public long addUser(UserRegisterRequest userRegisterRequest) {
        String userName = userRegisterRequest.getUserName();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        Integer userRole = userRegisterRequest. getUserRole();

        // 1. 校验
        if (StringUtils.isAnyBlank(userName, userPassword, checkPassword)) {
            throw new RuntimeException("参数为空");
        }
        if (userName.length() < 4) {
            throw new RuntimeException("用户名过短");
        }
        if (userPassword.length() < 8) {
            throw new RuntimeException("密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new RuntimeException("两次密码不一致");
        }

        // 2. 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName", userName);
        queryWrapper. eq("isDelete", 0);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 3. 加密密码
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 4. 插入数据
        User user = new User();
        user.setUserName(userName);
        user.setUserPassword(encryptPassword);
        // 🆕 设置角色（如果没传，默认为普通用户）
        user.setUserRole(userRole != null ? userRole : 0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new RuntimeException("添加失败");
        }

        return user.getId();
    }
}