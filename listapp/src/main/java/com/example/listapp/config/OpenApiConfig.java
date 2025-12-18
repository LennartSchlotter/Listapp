package com.example.listapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {
    
    @Value("${spring.security.oauth2.client.provider.google.authorization-uri:https://accounts.google.com/o/oauth2/v2/auth}")
    private String authorizationUri;

    @Value("${spring.security.oauth2.client.provider.google.token-uri:https://oauth2.googleapis.com/token}")
    private String tokenUri;

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
            .info(new Info()
                .title("Listapp API")
                .version("1.0")
                .description("API documentation"))
            .addSecurityItem(new SecurityRequirement().addList("oauth2"))
            .components(new Components().addSecuritySchemes("oauth2", new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows()
                    .authorizationCode(new OAuthFlow()
                        .authorizationUrl(authorizationUri)
                        .tokenUrl(tokenUri)
                        .scopes(new io.swagger.v3.oas.models.security.Scopes()
                            .addString("openid", "OpenID")
                            .addString("profile", "Profile")
                            .addString("email", "Email"))))));
    }
}
