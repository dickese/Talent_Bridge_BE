package com.example.demo.dto.request.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class UserRegisterRequest {
    private String fullName;
    private String email;
    private String address;
    private LocalDate dob;
    private String password;
    private boolean isRecruiter;
}
