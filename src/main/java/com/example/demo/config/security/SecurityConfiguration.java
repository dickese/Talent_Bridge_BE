package com.example.demo.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {
    
    private final JwtDecoder customJwtDecoder;
    private static final String[] WHITELIST = {
            // LOGIN
            "/auth/login",
            "/auth/logout",
            "/auth/register",
            "/auth/refresh-token",

            // BASIC MODULES
            "/companies/**",
            "/jobs/**",

            // API DOCS
            "/swagger-ui/**",
            "/v3/api-docs/**",

    };


    @Bean
    public SecurityFilterChain jwtFilterChain(HttpSecurity httpSecurity,
                                              CustomAuthenticationEntryPoint customerAuthenticationEntryPoint) throws Exception {

        log.info("Loading jwtFilterChain - Securing all other endpoints with JWT authentication");
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(customJwtDecoder))
                        .authenticationEntryPoint(customerAuthenticationEntryPoint)
                        .bearerTokenResolver(new SkipPathBearerTokenResolver())
                )
                .csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }
}
