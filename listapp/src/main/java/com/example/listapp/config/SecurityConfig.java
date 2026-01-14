package com.example.listapp.config;

import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.listapp.security.CustomCsrfTokenRequestHandler;
import com.example.listapp.security.OAuth2SuccessHandler;
import com.example.listapp.service.Security.AuthenticationService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Success Handler to use for OAuth2 login.
     */
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    /**
     * Authentication Service to use for OAuth2 login.
     */
    private final AuthenticationService authenticationService;

    /**
     * Defines the security filter chain for the application.
     * @param http The HttpSecurity configuration builder.
     * @return the configured SecurityFilterChain
     * @throws Exception if configuration fails.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http)
        throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf
                .csrfTokenRepository(
                    CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new CustomCsrfTokenRequestHandler())
            )
            .addFilterAfter(csrfTokenBootstrapFilter(), CsrfFilter.class)
            .securityContext(securityContext -> securityContext
                .requireExplicitSave(false)
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .sessionFixation().none()
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/",
                    "/login",
                    "/oauth2/**",
                    "/login/oauth2/**",
                    "/error",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .oidcUserService(authenticationService)
                )
                .successHandler(oAuth2SuccessHandler)
            )
            .exceptionHandling(exception -> exception
                .defaultAuthenticationEntryPointFor(
                    (request, response, authException) -> response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED),
                    request -> request.getRequestURI().startsWith("/api/")
                )
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "XSRF-TOKEN")
            );

        return http.build();
    }

    /**
     * Provides the CORS configuration source.
     * @return the UrlBasedCorsConfigurationSource with applied configuration.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of(
            "http://localhost:5173", "http://localhost:8080"));
        configuration.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Forces eager initialization of the CSRF token.
     * @return a filter instance to be registered in the filter chain.
     */
    @Bean
    public OncePerRequestFilter csrfTokenBootstrapFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(
                final HttpServletRequest request,
                final HttpServletResponse response,
                final FilterChain filterChain
            ) throws ServletException, IOException {

                Object csrfAttr = request.getAttribute(
                    CsrfToken.class.getName());
                if (csrfAttr instanceof CsrfToken csrfToken) {
                    csrfToken.getToken();
                }

                filterChain.doFilter(request, response);
            }
        };
    }
}
