package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T>{
    private boolean success;
    private String message;
    private T data;
    private String errorCode;
    private List<ErrorField> errorFields;
    private Instant responseTime;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ErrorField{
        private String field;
        private String message;
    }
}
