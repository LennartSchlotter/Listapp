package com.example.listapp.security;

import java.util.function.Supplier;

import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {
    private final CsrfTokenRequestHandler delegate = new XorCsrfTokenRequestAttributeHandler();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> token) {
        this.delegate.handle(request, response, token);
    }

    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken token) {
        if (StringUtils.hasText(request.getHeader(token.getHeaderName()))) {
            return super.resolveCsrfTokenValue(request, token);
        }

        return this.delegate.resolveCsrfTokenValue(request, token);
    }
}
