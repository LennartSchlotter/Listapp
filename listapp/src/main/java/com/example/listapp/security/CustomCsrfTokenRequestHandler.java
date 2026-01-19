package com.example.listapp.security;

import java.util.function.Supplier;

import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomCsrfTokenRequestHandler
    extends CsrfTokenRequestAttributeHandler {

    /**
     * Delegate for the CsrfToken Request Attribute Handler.
     */
    private final CsrfTokenRequestHandler delegate =
        new XorCsrfTokenRequestAttributeHandler();

    /**
     * Makes the expected CSRF token available to the rest of the application.
     */
    @Override
    public void handle(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Supplier<CsrfToken> token
    ) {
        this.delegate.handle(request, response, token);
    }

    /**
     * Extracts the client-submitted CSRF token value for comparison.
     */
    @Override
    public String resolveCsrfTokenValue(
        final HttpServletRequest request,
        final CsrfToken token
    ) {
        if (StringUtils.hasText(request.getHeader(token.getHeaderName()))) {
            return super.resolveCsrfTokenValue(request, token);
        }

        return this.delegate.resolveCsrfTokenValue(request, token);
    }
}
