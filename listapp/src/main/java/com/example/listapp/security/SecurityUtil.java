package com.example.listapp.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.listapp.entity.User;
import com.example.listapp.exception.custom.AccessDeniedException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    /**
     * Helper method to authenticate the current user.
     * @return the currently authenticated user.
     */
    public User getCurrentUser() {
        var authentication = SecurityContextHolder
            .getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomOidcUser user) {
            return user.getUser();
        } else {
            throw new AccessDeniedException("Invalid principal type");
        }
    }
}
