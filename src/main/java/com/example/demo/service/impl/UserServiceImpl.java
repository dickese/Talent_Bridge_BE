package com.example.demo.service.impl;

import com.example.demo.dto.request.user.*;
import com.example.demo.dto.response.user.DefaultUserResponse;
import com.example.demo.dto.response.user.UserDetailsResponse;
import com.example.demo.model.domain.user.Role;
import com.example.demo.model.domain.user.User;
import com.example.demo.model.domain.user.UserStatus;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final String DEFAULT_PASSWORD = "12345678";
    private final AuthService authService;

    @Override
    public DefaultUserResponse createUser(UserCreationRequest request) {
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        String userPassword = passwordEncoder.encode(DEFAULT_PASSWORD);
        User user = new User(request.getEmail(), request.getName(), userPassword, role);
        return mapEntityToDefaultResponse(userRepository.saveAndFlush(user));
    }

    @Override
    public List<DefaultUserResponse> getAllUser(Pageable pageable) {
        return userRepository.findAll(pageable).stream().map(this::mapEntityToDefaultResponse).toList();
    }

    @Override
    public UserDetailsResponse getUserDetails(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return mapEntityToUserDetails(user);
    }

    @Override
    public DefaultUserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return mapEntityToDefaultResponse(user);
    }

    @Override
    public DefaultUserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Update fields
        if (request.getName() != null) {
            user.setFullName(request.getName());
        }
        if (request.getRoleId() != null) {
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new EntityNotFoundException("Role not found"));
            user.setRole(role);
        }
        // Add other fields if needed

        return mapEntityToDefaultResponse(userRepository.saveAndFlush(user));
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public DefaultUserResponse selfUpdatePassword(SelfUpdatePasswordRequest request) {
        User user = authService.getCurrentUser();

        if(user.getStatus().equals(UserStatus.ACTIVE)){
            throw new IllegalStateException("Inactive user can't update password");
        }

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return mapEntityToDefaultResponse(userRepository.saveAndFlush(user));
    }

    @Override
    public DefaultUserResponse selfUpdateAvatar(MultipartFile file) {
        User user = authService.getCurrentUser();

        if(user.getStatus().equals(UserStatus.ACTIVE)){
            throw new IllegalStateException("Inactive user can't update avatar");
        }

        return null;
    }

    private DefaultUserResponse mapEntityToDefaultResponse(User user){
        Role role = user.getRole();
        List<String> permissions = role != null && role.getPermissions() != null
                ? role.getPermissions().stream().map(p -> p.getMethod() + " " + p.getApiPath()).toList()
                : List.of();

        return new DefaultUserResponse(
                user.getId(),
                user.getFullName(),
                user.getAvatarUrl(),
                role.getName().toString(),
                permissions,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private UserDetailsResponse mapEntityToUserDetails(User user){
        return new UserDetailsResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getGender(),
                user.getAddress(),
                user.getDob(),
                user.getAvatarUrl(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }


}
