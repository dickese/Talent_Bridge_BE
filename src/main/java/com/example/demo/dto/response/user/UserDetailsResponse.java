package com.example.demo.dto.response.user;


import com.example.demo.model.common.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@Data
@Builder
public class UserDetailsResponse {
    private Long id;
    private String name;
    private String email;
    private Gender gender;
    private String address;
    private LocalDate dob;
    private String avatarUrl;
    private Instant createdAt;
    private Instant updatedAt;
}
