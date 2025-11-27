package com.yclin.quiz.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JwtUtil 单元测试
 */
public class JwtUtilTest {

    @Test
    public void testGenerateAndValidateToken() {
        // 生成 Token
        Long userId = 1L;
        String userName = "testuser";
        Integer userRole = 0;

        String token = JwtUtil.generateToken(userId, userName, userRole);

        // 验证 Token 不为空
        assertNotNull(token);
        assertTrue(token.length() > 0);

        // 验证 Token 有效
        assertTrue(JwtUtil.validateToken(token));

        // 验证用户信息提取
        assertEquals(userId, JwtUtil.getUserId(token));
        assertEquals(userName, JwtUtil.getUserName(token));
        assertEquals(userRole, JwtUtil.getUserRole(token));
    }

    @Test
    public void testInvalidToken() {
        // 验证无效 Token
        assertFalse(JwtUtil.validateToken("invalid-token"));
        assertFalse(JwtUtil.validateToken(""));
        assertFalse(JwtUtil.validateToken(null));
    }

    @Test
    public void testParseInvalidToken() {
        // 解析无效 Token 应返回 null
        assertNull(JwtUtil.parseToken("invalid-token"));
        assertNull(JwtUtil.getUserId("invalid-token"));
        assertNull(JwtUtil.getUserName("invalid-token"));
        assertNull(JwtUtil.getUserRole("invalid-token"));
    }
}
