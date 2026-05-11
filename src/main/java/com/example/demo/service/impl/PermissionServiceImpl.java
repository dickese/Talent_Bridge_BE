package com.example.demo.service.impl;

import com.example.demo.advice.exception.ResourceAlreadyExistsException;
import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.permission.DefaultPermissionRequest;
import com.example.demo.dto.response.permission.DefaultPermissionResponse;
import com.example.demo.dto.response.role.DefaultRoleResponse;
import com.example.demo.model.domain.user.Permission;
import com.example.demo.model.domain.user.Role;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.PermissionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    public PageResponseDto<DefaultPermissionResponse> findAllPermission(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pagePermission = permissionRepository.findAll(spec,pageable);

        List<DefaultPermissionResponse> permissionResponses = pagePermission.getContent()
                .stream()
                .map(this::mapToDefaultResponseDto)
                .toList();

        return PageResponseDto.<DefaultPermissionResponse>builder()
                .content(permissionResponses)
                .page(pagePermission.getNumber() + 1)
                .size(pagePermission.getSize())
                .totalElements(pagePermission.getTotalElements())
                .totalPages(pagePermission.getTotalPages())
                .build();
    }

    @Override
    public Page<DefaultPermissionResponse> findAllPermissionNoPaging(Specification<Permission> spec, Pageable pageable) {
        return permissionRepository
                .findAll(spec, pageable)
                .map(this::mapToDefaultResponseDto);
    }

    @Override
    public DefaultPermissionResponse savePermission(DefaultPermissionRequest defaultPermissionRequestDto) {
        boolean existsByPathAndMethod = permissionRepository.existsByApiPathAndMethod(
                defaultPermissionRequestDto.getApiPath(),
                defaultPermissionRequestDto.getMethod()
        );

        if (existsByPathAndMethod) {
            throw new ResourceAlreadyExistsException("Permission with the same API path and method already exists");
        }


        Permission permission = new Permission(
                defaultPermissionRequestDto.getName(),
                defaultPermissionRequestDto.getApiPath(),
                defaultPermissionRequestDto.getMethod(),
                defaultPermissionRequestDto.getModule(),
                defaultPermissionRequestDto.getDescription()
        );

        Permission savedPermission = permissionRepository.save(permission);
        return mapToDefaultResponseDto(savedPermission);
    }

    @Override
    public DefaultPermissionResponse updatePermission(Long id, DefaultPermissionRequest defaultPermissionRequestDto) {
        return null;
    }

    @Override
    @Transactional
    public DefaultPermissionResponse deletePermission(Long id) {
        Permission permission = permissionRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found"));

        roleRepository.removePermissionFromRoles(id);

        permissionRepository.delete(permission);
        return mapToDefaultResponseDto(permission);
    }

    private DefaultPermissionResponse mapToDefaultResponseDto(Permission permission) {

        DefaultPermissionResponse res = new DefaultPermissionResponse(
                permission.getId(),
                permission.getName(),
                permission.getApiPath(),
                permission.getMethod(),
                permission.getModule(),
                permission.getCreatedAt().toString(),
                permission.getUpdatedAt().toString()
        );

        return res;
    }
}
