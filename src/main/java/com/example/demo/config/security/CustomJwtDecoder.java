package com.example.demo.config.security;

import com.example.demo.advice.exception.UnauthorizedException;
import com.example.demo.config.auth.AuthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    private final NimbusJwtDecoder decoder;

    public CustomJwtDecoder(AuthConfig authConfig) {
        this.decoder = NimbusJwtDecoder
                .withSecretKey(authConfig.getSecretKey())
                .build();

        OAuth2TokenValidator<Jwt> withDefaults =
                JwtValidators.createDefault();

        OAuth2TokenValidator<Jwt> accessTypeValidator =
                jwt -> {
                    if (!"access".equals(jwt.getClaim("typ"))) {
                        return OAuth2TokenValidatorResult.failure(
                                new OAuth2Error("invalid_token", "Not access token", null)
                        );
                    }
                    return OAuth2TokenValidatorResult.success();
                };

        decoder.setJwtValidator(
                new DelegatingOAuth2TokenValidator<>(
                        withDefaults,
                        accessTypeValidator
                )
        );
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            Jwt jwt = decoder.decode(token);

            // Debug logging cho thời gian
            Instant now = Instant.now();
            Instant issuedAt = jwt.getIssuedAt();
            Instant expiresAt = jwt.getExpiresAt();

            System.out.println("=== JWT DEBUG INFO ===");
            System.out.println("Current time (UTC): " + now);
            System.out.println("Current time (System): " + now.atZone(ZoneId.systemDefault()));
            System.out.println("Token issuedAt: " + issuedAt);
            System.out.println("Token expiresAt: " + expiresAt);
            System.out.println("Time until expiry: " + java.time.Duration.between(now, expiresAt));
            System.out.println("Is expired: " + now.isAfter(expiresAt));
            System.out.println("System timezone: " + ZoneId.systemDefault());
            System.out.println("======================");

            return jwt;
        } catch (JwtException e) {
            // Debug logging khi có lỗi
            System.out.println("=== JWT DECODE ERROR ===");
            System.out.println("Error: " + e.getMessage());
            System.out.println("Current time (UTC): " + Instant.now());
            System.out.println("Current time (System): " + Instant.now().atZone(ZoneId.systemDefault()));
            System.out.println("System timezone: " + ZoneId.systemDefault());
            System.out.println("========================");
            throw e;
        }
    }
}
