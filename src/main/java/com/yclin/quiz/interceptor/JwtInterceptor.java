package com.yclin.quiz.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yclin.quiz.context.UserContext;
import com.yclin.quiz.model.domain.Result;
import com.yclin.quiz.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 拦截器
 * 验证请求中的 Token，并将用户信息存入 UserContext
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行 OPTIONS 请求（CORS 预检）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 从请求头中获取 Token
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendError(response, "未提供有效的认证Token");
            return false;
        }

        // 提取 Token（去掉 "Bearer " 前缀）
        String token = authHeader.substring(7);

        // 验证 Token
        if (!JwtUtil.validateToken(token)) {
            sendError(response, "Token无效或已过期");
            return false;
        }

        // 从 Token 中提取用户信息并存入 UserContext
        Long userId = JwtUtil.getUserId(token);
        String userName = JwtUtil.getUserName(token);
        Integer userRole = JwtUtil.getUserRole(token);

        UserContext.setCurrentUser(userId, userName, userRole);
        log.debug("用户认证成功：userId={}, userName={}", userId, userName);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除 UserContext，防止内存泄漏
        UserContext.clear();
    }

    /**
     * 发送错误响应
     *
     * @param response HTTP 响应
     * @param message  错误信息
     */
    private void sendError(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        Result result = Result.error(message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
