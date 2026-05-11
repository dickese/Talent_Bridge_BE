package com.example.demo.service;

import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.role.DefaultRoleRequest;
import com.example.demo.dto.response.role.DefaultRoleResponse;
import com.example.demo.model.domain.user.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface RoleService {
    DefaultRoleResponse saveRole(DefaultRoleRequest defaultRoleRequestDto);

    DefaultRoleResponse updateRole(
            Long id,
            DefaultRoleRequest defaultRoleRequestDto
    );

    PageResponseDto<DefaultRoleResponse> findAllRoles(
            Specification<Role> spec,
            Pageable pageable);

    DefaultRoleResponse deleteRoleById(Long id);
}
