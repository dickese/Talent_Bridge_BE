package com.example.demo.controller;

import com.example.demo.anotation.ApiMessage;
import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.permission.DefaultPermissionRequest;
import com.example.demo.dto.response.permission.DefaultPermissionResponse;
import com.example.demo.model.domain.user.Permission;
import com.example.demo.service.PermissionService;
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

import java.util.List;

/**
 * PermissionController - Quản lý các permission/quyền hạn trong hệ thống
 * Endpoints: GET /permissions/*
 */
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('GET /permissions/*')")
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping
    @ApiMessage(value = "Tạo quyền hạn")
    @Operation(
            summary = "Tạo quyền hạn",
            description = "Yêu cầu quyền: <b>'GET /permissions/*</b>"
    )
    public DefaultPermissionResponse savePermission(
            @Valid @RequestBody DefaultPermissionRequest defaultPermissionRequestDto
    ) {
        return permissionService.savePermission(defaultPermissionRequestDto);
    }


    @GetMapping
    @ApiMessage("Lấy danh sách quyền hạn")
    @Operation(
            summary = "Lấy danh sách quyền hạn",
            description = "Yêu cầu quyền: <b>'GET /permissions/*</b>"
    )
    public ResponseEntity<?> findAllPermissions(
            @Filter Specification<Permission> spec,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        PageResponseDto<DefaultPermissionResponse> res = permissionService.findAllPermission(spec, pageable);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/all")
    @ApiMessage("Lấy toàn bộ quyền hạn (không phân trang)")
    @Operation(
            summary = "Lấy toàn bộ quyền hạn (không phân trang)",
            description = "Yêu cầu quyền: <b>'GET /permissions/*</b>"
    )
    public ResponseEntity<?> findAllPermissionsNoPaging(
            @Filter Specification<Permission> spec
    ) {
        List<DefaultPermissionResponse> list = permissionService
                .findAllPermissionNoPaging(spec, Pageable.unpaged())
                .getContent();

        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    @ApiMessage(value = "Cập nhật quyền hạn")
    @Operation(
            summary = "Cập nhật quyền hạn",
            description = "Yêu cầu quyền: <b>'GET /permissions/*</b>"
    )
    public DefaultPermissionResponse updatePermissionById(
            @Valid @RequestBody DefaultPermissionRequest defaultPermissionRequestDto,
            @PathVariable Long id
    ) {
        return permissionService.updatePermission(id, defaultPermissionRequestDto);
    }

    @DeleteMapping("/{id}")
    @ApiMessage(value = "Xóa quyền hạn")
    @Operation(
            summary = "Xóa quyền hạn",
            description = "Yêu cầu quyền: <b>'GET /permissions/*</b>"
    )
    public DefaultPermissionResponse deletePermissionById(
            @PathVariable Long id
    ) {
        return permissionService.deletePermission(id);
    }
}

