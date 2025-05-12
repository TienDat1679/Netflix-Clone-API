package com.backend.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {

    public static String getUserEmail() {
        var context = SecurityContextHolder.getContext();
        return context.getAuthentication().getName();
    }
    
}
