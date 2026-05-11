package com.example.demo.dto.response.permission;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DefaultPermissionResponse {
    private Long id;
    private String name;
    private String apiPath;
    private String method;
    private String module;
    private String createdAt;
    private String updatedAt;
}
