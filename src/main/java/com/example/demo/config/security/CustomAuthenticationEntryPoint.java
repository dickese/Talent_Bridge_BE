package com.example.demo.config.security;

import com.example.demo.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException ex
    ) throws IOException {

        log.error("ex" , ex);
        if (ex.getCause() != null) {
            log.error("Cause: {}", ex.getCause().getClass().getName());
        }

        String message;
        String errorCode;
        int status = HttpServletResponse.SC_UNAUTHORIZED;

        switch (ex) {
            case BadCredentialsException badCredentialsException -> {
                message = "Email hoặc mật khẩu không đúng";
                errorCode = "BAD_CREDENTIALS";
            }
            case UsernameNotFoundException usernameNotFoundException -> {
                message = "Tài khoản không tồn tại";
                errorCode = "USER_NOT_FOUND";
            }
            case InsufficientAuthenticationException insufficientAuthenticationException -> {
                message = "Yêu cầu đăng nhập";
                errorCode = "AUTH_REQUIRED";
            }
            case InvalidBearerTokenException invalidBearerTokenException -> {
                message = "Token không hợp lệ hoặc đã hết hạn";
                errorCode = "INVALID_TOKEN";
            }
            default -> {
                message = "Không thể xác thực người dùng";
                errorCode = "UNAUTHENTICATED";
            }
        }

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .responseTime(Instant.now())
                .build();

        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}