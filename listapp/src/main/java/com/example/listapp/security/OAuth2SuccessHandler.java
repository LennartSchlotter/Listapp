package com.example.listapp.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class OAuth2SuccessHandler
    implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Authentication authentication
    ) throws IOException, ServletException {

        response.sendRedirect("http://localhost:5173/oauth-callback");
    }

}
