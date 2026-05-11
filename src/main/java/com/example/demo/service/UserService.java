package com.example.demo.service;

import com.example.demo.dto.request.user.*;
import com.example.demo.dto.response.user.DefaultUserResponse;
import com.example.demo.dto.response.user.UserDetailsResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    DefaultUserResponse createUser(UserCreationRequest reuqest);
    List<DefaultUserResponse> getAllUser(Pageable pageable);
    UserDetailsResponse getUserDetails(Long id);
    DefaultUserResponse getUserById(Long id);
    DefaultUserResponse updateUser(Long id, UserUpdateRequest request);
    void deleteUser(Long id);
    DefaultUserResponse selfUpdatePassword(SelfUpdatePasswordRequest request);
    DefaultUserResponse selfUpdateAvatar(MultipartFile file);
}
