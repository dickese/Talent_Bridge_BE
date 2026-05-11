package com.example.demo.advice;


import com.example.demo.anotation.ApiMessage;
import com.example.demo.dto.ApiResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.Instant;
import java.time.LocalDateTime;


@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.getParameterType().equals(ApiResponse.class);
    }

    @Override
    public @Nullable Object beforeBodyWrite(@Nullable Object body,
                                            MethodParameter returnType,
                                            MediaType selectedContentType,
                                            Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                            ServerHttpRequest request,
                                            ServerHttpResponse response) {

        if (body instanceof ApiResponse<?>)
            return body;

        ApiMessage apiMessage = returnType.getMethodAnnotation(ApiMessage.class);
        return ApiResponse.builder()
                .success(true)
                .message(apiMessage == null ? "Success" : apiMessage.value())
                .data(body)
                .responseTime(Instant.now())
                .build();
    }
}
