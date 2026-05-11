package com.example.demo.controller;

import com.example.demo.anotation.ApiMessage;
import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.role.DefaultRoleRequest;
import com.example.demo.dto.response.role.DefaultRoleResponse;
import com.example.demo.model.domain.user.Role;
import com.example.demo.service.RoleService;
import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

/**
 * RoleController - Quản lý các role trong hệ thống
 * Endpoints: POST, GET, PUT, DELETE /roles
 */
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @ApiMessage(value = "Tạo Role")
    @PreAuthorize("hasAuthority('POST /roles')")
    @Operation(
            summary = "Tạo Role",
            description = "Yêu cầu quyền: <b>POST /roles</b>"
    )
    public ResponseEntity<DefaultRoleResponse> saveRole(
            @Valid @RequestBody DefaultRoleRequest defaultRoleRequestDto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roleService.saveRole(defaultRoleRequestDto));
    }

    @GetMapping
    @ApiMessage(value = "Lấy danh sách Role")
    @PreAuthorize("hasAuthority('GET /roles')")
    @Operation(
            summary = "Lấy danh sách Role",
            description = "Yêu cầu quyền: <b>GET /roles</b>"
    )
    public ResponseEntity<PageResponseDto<DefaultRoleResponse>> findAllRoles(
            @Filter Specification<Role> spec,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        PageResponseDto<DefaultRoleResponse> res = roleService.findAllRoles(spec, pageable);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    @ApiMessage(value = "Cập nhật Role")
    @PreAuthorize("hasAuthority('PUT /roles/{id}')")
    @Operation(
            summary = "Câp nhật Role",
            description = "Yêu cầu quyền: <b>PUT /roles/{id}</b>"
    )
    public ResponseEntity<DefaultRoleResponse> updateRoleById(
            @PathVariable Long id,
            @Valid @RequestBody DefaultRoleRequest defaultRoleRequestDto
    ) {
        return ResponseEntity.ok(roleService.updateRole(id, defaultRoleRequestDto));
    }

    @DeleteMapping("/{id}")
    @ApiMessage(value = "Xóa Role theo id")
    @PreAuthorize("hasAuthority('DELETE /roles/{id}')")
    @Operation(
            summary = "Xóa Role theo id",
            description = "Yêu cầu quyền: <b>DELETE /roles/{id}</b>"
    )
    public ResponseEntity<DefaultRoleResponse> deleteRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.deleteRoleById(id));
    }
}

