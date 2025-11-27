package com.yclin.quiz.context;

/**
 * 用户上下文工具类
 * 使用 ThreadLocal 存储当前登录用户信息
 */
public class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_NAME = new ThreadLocal<>();
    private static final ThreadLocal<Integer> USER_ROLE = new ThreadLocal<>();

    /**
     * 设置当前用户信息
     *
     * @param userId   用户ID
     * @param userName 用户名
     * @param userRole 用户角色
     */
    public static void setCurrentUser(Long userId, String userName, Integer userRole) {
        USER_ID.set(userId);
        USER_NAME.set(userName);
        USER_ROLE.set(userRole);
    }

    /**
     * 获取当前用户ID
     *
     * @return 用户ID
     */
    public static Long getCurrentUserId() {
        return USER_ID.get();
    }

    /**
     * 获取当前用户名
     *
     * @return 用户名
     */
    public static String getCurrentUserName() {
        return USER_NAME.get();
    }

    /**
     * 获取当前用户角色
     *
     * @return 用户角色
     */
    public static Integer getCurrentUserRole() {
        return USER_ROLE.get();
    }

    /**
     * 清除当前用户信息
     * 在请求结束后调用，防止内存泄漏
     */
    public static void clear() {
        USER_ID.remove();
        USER_NAME.remove();
        USER_ROLE.remove();
    }
}
