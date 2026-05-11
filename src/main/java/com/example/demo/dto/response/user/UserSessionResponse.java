package com.example.demo.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSessionResponse {
    private Long id;
    private String email;
    private String name;
    private String status;
    private String avatarUrl;
    private boolean verifiedEmail;
    private String role;
    private List<String> permissions;
    private Long companyId;
}
