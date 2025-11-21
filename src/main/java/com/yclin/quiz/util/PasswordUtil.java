package com.yclin.quiz.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 密码加密/校验工具，使用 PBKDF2WithHmacSHA256
 * 存储格式: iterations$saltBase64$hashBase64
 */
public class PasswordUtil {
    private static final String ALGO = "PBKDF2WithHmacSHA256";
    private static final int SALT_LEN = 16; // bytes
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256; // bits

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String hashPassword(String password) {
        try {
            byte[] salt = new byte[SALT_LEN];
            RANDOM.nextBytes(salt);
            byte[] hash = pbkdf2(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            String saltB64 = Base64.getEncoder().encodeToString(salt);
            String hashB64 = Base64.getEncoder().encodeToString(hash);
            return ITERATIONS + "$" + saltB64 + "$" + hashB64;
        } catch (Exception e) {
            throw new RuntimeException("密码加密失败", e);
        }
    }

    public static boolean verifyPassword(String password, String stored) {
        try {
            if (stored == null) return false;
            String[] parts = stored.split("\\$");
            if (parts.length != 3) return false;
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] hash = Base64.getDecoder().decode(parts[2]);

            byte[] calcHash = pbkdf2(password.toCharArray(), salt, iterations, hash.length * 8);
            // 固定时间比较
            return slowEquals(hash, calcHash);
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLength) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGO);
        return skf.generateSecret(spec).getEncoded();
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < Math.min(a.length, b.length); i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}