package com.example.demo.service;

import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.permission.DefaultPermissionRequest;
import com.example.demo.dto.response.permission.DefaultPermissionResponse;
import com.example.demo.model.domain.user.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface PermissionService {
    PageResponseDto<DefaultPermissionResponse> findAllPermission(
            Specification<Permission> spec,
            Pageable pageable
    );

    Page<DefaultPermissionResponse> findAllPermissionNoPaging(
            Specification<Permission> spec,
            Pageable pageable
    );

    DefaultPermissionResponse savePermission(
            DefaultPermissionRequest defaultPermissionRequestDto
    );

    DefaultPermissionResponse updatePermission(
            Long id,
            DefaultPermissionRequest defaultPermissionRequestDto
    );

    DefaultPermissionResponse deletePermission(Long id);
}
