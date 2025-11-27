package com. yclin.quiz.interceptor;

import com.fasterxml.jackson.databind. ObjectMapper;
import com. yclin.quiz.model.domain.Result;
import com.yclin.quiz.util.JwtUtil;
import lombok.extern.slf4j. Slf4j;
import org. springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet. http.HttpServletResponse;

/**
 * JWT 权限拦截器
 */
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 跨域预检请求直接放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");

        // 去掉 "Bearer " 前缀
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证 Token
        if (token == null || token.isEmpty()) {
            sendErrorResponse(response, "未登录");
            return false;
        }

        if (! jwtUtil.validateToken(token)) {
            sendErrorResponse(response, "Token已过期或无效");
            return false;
        }

        // Token 有效，继续执行
        return true;
    }

    /**
     * 发送错误响应
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws Exception {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");

        Result result = Result.error(message);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);

        response.getWriter().write(json);
    }
}