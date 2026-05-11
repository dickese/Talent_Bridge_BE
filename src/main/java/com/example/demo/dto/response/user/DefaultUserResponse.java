package com.example.demo.dto.response.user;

import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DefaultUserResponse {
    private Long id;
    private String name;
    private String avatarUrl;
    private String role;
    private List<String> permissions;
    private Instant createdAt;
    private Instant updateAt;
}
