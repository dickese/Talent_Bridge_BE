package com.example.demo.dto.response.role;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DefaultRoleResponse {
    private Long id;
    private String name;
    private String description;
    private boolean active;
    private String createdAt;
    private String updatedAt;
    private List<PermissionResponse> permissions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PermissionResponse {
        private Long id;
        private String name;
        private String apiPath;
        private String method;
        private String module;
    }

    public DefaultRoleResponse(
            Long id, boolean active, String name,
            String createdAt, String updatedAt, String description
    ) {
        this.id = id;
        this.active = active;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.description = description;
    }
}
