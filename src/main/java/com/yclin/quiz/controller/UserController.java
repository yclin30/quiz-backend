package com.yclin.quiz.controller;

import com.yclin.quiz.dto.UserLoginRequest;
import com.yclin.quiz.dto.UserRegisterRequest;
import com.yclin.quiz.model.domain.PageBean;
import com.yclin.quiz.model.domain.Result;
import com.yclin.quiz.model.domain.User;
import com.yclin.quiz.service.UserService;
import com.yclin.quiz.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public Result userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return Result.error("参数为空");
        }
        try {
            long result = userService.userRegister(userRegisterRequest);
            return Result.success("注册成功", result);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable("id") Long id) {

        try {
            boolean result = userService.deleteUserById(id);
            return result ? Result.success("删除成功") : Result.error("删除失败");
        } catch (RuntimeException e) {

            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据用户名删除用户
     */
    @DeleteMapping("/name/{username}")
    public Result deleteUserByName(@PathVariable("username") String username) {
        try {
            boolean result = userService.deleteUserByName(username);
            return result ? Result.success("删除成功") : Result.error("删除失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    /**
     * 硬删除 - 根据ID
     */
    @DeleteMapping("/hard/{id}")
    public Result hardDeleteUser(@PathVariable("id") Long id) {

        try {
            boolean result = userService.hardDeleteById(id);
            return result ? Result.success("删除成功") : Result.error("删除失败");
        } catch (RuntimeException e) {

            return Result.error(e.getMessage());
        }
    }

    /**
     * 硬删除 - 根据用户名
     */
    @DeleteMapping("/hard/name/{username}")
    public Result hardDeleteUserByName(@PathVariable("username") String username) {
        try {
            boolean result = userService.hardDeleteByName(username);
            return result ? Result.success("删除成功") : Result.error("删除失败");
        } catch (RuntimeException e) {

            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable("id") Long id) {
        try {
            User user = userService.getUserById(id);
            return Result.success(user);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 搜索用户
     * @param username 用户名（可选）
     * @return 用户列表
     */
    @GetMapping("/search")
    public Result searchUsers(@RequestParam(required = false) String username) {
        try {
            List<User> userList = userService.searchUsers(username);
            return Result.success(userList);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/users")
    public Result getPage(@RequestParam(defaultValue="1")Integer page, @RequestParam(defaultValue="5")Integer pageSize){
        PageBean pageBean=userService.page(page, pageSize);
        return Result.success(pageBean);
    }

    @GetMapping("/findUser")
    public Result getUser(String keyword){
        List<User> users=userService.findByName(keyword);
        return Result.success(users);
    }

    @PostMapping("/login")
    public Result userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null) {
            return Result.error("参数为空");
        }
        try {
            User user = userService.userLogin(userLoginRequest);

            // 生成 Token
            String token = jwtUtil.generateToken(
                    user.getId(),
                    user.getUserName(),
                    user.getUserRole()
            );

            // 返回用户信息和 Token
            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            data.put("token", token);

            return Result.success("登录成功", data);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/current")
    public Result getCurrentUser(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return Result.error("未登录");
        }

        try {
            // 去掉 "Bearer " 前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // 验证 Token
            if (!jwtUtil.validateToken(token)) {
                return Result.error("Token已过期");
            }

            // 获取用户信息
            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getUserById(userId);

            return Result.success(user);
        } catch (Exception e) {
            return Result.error("Token无效");
        }
    }
    /**
     * 🆕 重置用户密码为 "123456"
     */
    @PutMapping("/resetPassword/{id}")
    public Result resetPassword(@PathVariable("id") Long id) {
        try {
            boolean result = userService.resetPassword(id);
            return result ?  Result.success("密码已重置为：123456") : Result.error("重置失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 🆕 管理员添加用户（可选择角色）
     */
    @PostMapping("/add")
    public Result addUser(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return Result.error("参数为空");
        }
        try {
            long result = userService.addUser(userRegisterRequest);
            return Result.success("添加成功", result);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}