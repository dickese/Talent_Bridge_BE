package com.example.demo.controller;

import com.example.demo.dto.request.user.UserCreationRequest;
import com.example.demo.dto.request.user.UserUpdateRequest;
import com.example.demo.dto.response.user.DefaultUserResponse;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @GetMapping
    @PreAuthorize("hasAuthority('GET /users')")
    public List<DefaultUserResponse> getAllUser(@PageableDefault(size = 5, page = 0) Pageable pageable){
        return userService.getAllUser(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('POST /users')")
    public DefaultUserResponse createUser(@RequestBody UserCreationRequest request){
        return userService.createUser(request);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PUT /users/{id}')")
    public DefaultUserResponse updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request){
        return userService.updateUser(id, request);
    }

    @PutMapping("/me/avatar")
    @PreAuthorize("hasAuthority('PATCH /users/me/avatar')")
    public DefaultUserResponse updateSelfAvatar(@RequestPart(value = "avatar") MultipartFile file){
        return userService.selfUpdateAvatar(file);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GET /users/{id}')")
    public DefaultUserResponse getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE /users/{id}')")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
}
