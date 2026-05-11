package com.example.demo.service.impl;

import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.role.DefaultRoleRequest;
import com.example.demo.dto.response.role.DefaultRoleResponse;
import com.example.demo.model.domain.user.Permission;
import com.example.demo.model.domain.user.Role;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public DefaultRoleResponse saveRole(DefaultRoleRequest defaultRoleRequestDto) {
        Role role = new Role(
                defaultRoleRequestDto.getName(),
                defaultRoleRequestDto.getDescription()
        );

        Set<Permission> permissions = null;
        if (defaultRoleRequestDto.getPermissions() != null) {
            Set<Long> permissionIds = defaultRoleRequestDto
                    .getPermissions()
                    .stream()
                    .map(DefaultRoleRequest.PermissionId::getId)
                    .collect(Collectors.toSet());
            permissions = new HashSet<>(permissionRepository.findAllById(permissionIds));

            if(permissions.size() != permissionIds.size()) {
                Set<Long> foundPermissionIds = permissions.stream()
                        .map(Permission::getId)
                        .collect(Collectors.toSet());
                permissionIds.removeAll(foundPermissionIds);
                throw new EntityNotFoundException("Permissions not found with ids: " + permissionIds);
            }
        }
        role.setPermissions(permissions);

        Role savedRole = roleRepository.save(role);
        return mapToDefaultRoleResponseDto(savedRole);
    }

    @Override
    public DefaultRoleResponse updateRole(Long id, DefaultRoleRequest defaultRoleRequestDto) {
        return null;
    }

    @Override
    public PageResponseDto<DefaultRoleResponse> findAllRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> pageRole = roleRepository.findAll(spec,pageable);

        List<DefaultRoleResponse> roleResponses = pageRole.getContent()
                .stream()
                .map(this::mapToDefaultRoleResponseDto)
                .toList();

        return PageResponseDto.<DefaultRoleResponse>builder()
                .content(roleResponses)
                .page(pageRole.getNumber() + 1)
                .size(pageRole.getSize())
                .totalElements(pageRole.getTotalElements())
                .totalPages(pageRole.getTotalPages())
                .build();
    }

    @Override
    public DefaultRoleResponse deleteRoleById(Long id) {
        Role role = roleRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        String currentName = role.getName();
        if (
                currentName != null
                        && !currentName.equalsIgnoreCase("ADMIN")
                        && !currentName.equalsIgnoreCase("USER")
        ) {
            DefaultRoleResponse defaultRoleResponseDto = mapToDefaultRoleResponseDto(role);

            if (role.getPermissions() != null) role.getPermissions().clear();
            userRepository.detachUsersFromRole(role.getId());

            roleRepository.delete(role);
            return defaultRoleResponseDto;
        }

        throw new AccessDeniedException("Không thể xóa chức vụ mặc định");
    }

    private DefaultRoleResponse mapToDefaultRoleResponseDto(Role role) {
        DefaultRoleResponse res = new DefaultRoleResponse(
                role.getId(),
                role.isActive(),
                role.getName().toString(),
                role.getCreatedAt().toString(),
                role.getUpdatedAt().toString(),
                role.getDescription()
        );

        List<DefaultRoleResponse.PermissionResponse> permissions = role.getPermissions()
                .stream()
                .map(p -> new DefaultRoleResponse.PermissionResponse(
                        p.getId(),
                        p.getName(),
                        p.getApiPath(),
                        p.getMethod(),
                        p.getModule()
                ))
                .toList();
        res.setPermissions(permissions);

        return res;
    }
}
