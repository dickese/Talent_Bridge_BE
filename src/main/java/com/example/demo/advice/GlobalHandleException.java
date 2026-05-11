package com.example.demo.advice;

import com.example.demo.advice.exception.*;
import com.example.demo.dto.ApiResponse;
import com.example.demo.model.persistence.emailToken.VerifiedErrorTokenResult;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.Instant;

@RestControllerAdvice
public class GlobalHandleException {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException ex){
        return ResponseEntity.badRequest()
                .body(ApiResponse.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .errorCode("BUSINESS_EXCEPTION")
                        .responseTime(Instant.now())
                        .build());
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleBadCredentialsException(BadCredentialsException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.builder()
                    .success(false)
                    .message("Email or password is incorrect")
                    .errorCode("UNAUTHORIZED")
                    .responseTime(Instant.now())
                    .build());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<?>> handleUnauthorizedException(UnauthorizedException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .errorCode("UNAUTHORIZED")
                        .responseTime(Instant.now())
                        .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException ex){
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .responseTime(Instant.now())
                        .build());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleBadRequestException(BadRequestException ex){
        return ResponseEntity.badRequest()
                .body(ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .responseTime(Instant.now())
                .build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
        return ResponseEntity.badRequest()
                .body(ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .responseTime(Instant.now())
                .build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleEntityNotFoundException(EntityNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .responseTime(Instant.now())
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .responseTime(Instant.now())
                .build());
    }

//    @ExceptionHandler(VerifiedTokenExpiredException.class)
//    public ResponseEntity<ApiResponse<?>> handleVerifiedTokenExpiredException(VerifiedTokenExpiredException ex){
//        VerifiedErrorTokenResult tokenExpiredResult = VerifiedErrorTokenResult.TOKEN_EXPIRED;
//        return ResponseEntity.status(HttpStatus.GONE).body(ApiResponse.builder()
//                .success(false)
//                .ApiMessage(ex.getMessage())
//                .errorCode(tokenExpiredResult.name())
//                .build());
//    }

//    @ExceptionHandler(VerifiedInvalidTokenException.class)
//    public ResponseEntity<ApiResponse<?>> handleVerifiedInvalidTokenException(VerifiedInvalidTokenException ex){
//        VerifiedErrorTokenResult invalidTokenResult = VerifiedErrorTokenResult.INVALID_TOKEN;
//        return ResponseEntity.status(HttpStatus.GONE).body(ApiResponse.builder()
//                .success(false)
//                .ApiMessage(ex.getMessage())
//                .errorCode(invalidTokenResult.name())
//                .build());
//    }
}
