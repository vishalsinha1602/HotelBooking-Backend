package com.project.hotelbooking.util;

import com.project.hotelbooking.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public  class AppUtil {
    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
