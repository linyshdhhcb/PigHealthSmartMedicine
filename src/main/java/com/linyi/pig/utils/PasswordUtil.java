package com.linyi.pig.utils;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author: linyi
 * @Date: 2025/2/25
 * @ClassName: PasswordUtil
 * @Version: 1.0
 * @Description:
 */
public class PasswordUtil {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
