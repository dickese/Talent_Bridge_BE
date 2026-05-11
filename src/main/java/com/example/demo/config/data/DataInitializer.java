package com.example.demo.config.data;

import com.example.demo.model.domain.user.Role;
import com.example.demo.model.domain.user.RoleName;
import com.example.demo.model.domain.user.User;
import com.example.demo.model.domain.user.UserStatus;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "admin123456";
    private static final String ADMIN_FULL_NAME = "Administrator";
    
    private static final String USER_EMAIL = "user@gmail.com";
    private static final String USER_PASSWORD = "user123456";
    private static final String USER_FULL_NAME = "Normal User";
    
    private static final String RECRUITER_EMAIL = "recruiter@gmail.com";
    private static final String RECRUITER_PASSWORD = "recruiter123456";
    private static final String RECRUITER_FULL_NAME = "Recruiter User";

    @Override
    public void run(ApplicationArguments args) {
        initializeAdminUser();
        initializeNormalUser();
        initializeRecruiterUser();
    }

    private void initializeAdminUser() {
        try {
            // Kiểm tra xem user admin đã tồn tại chưa
            if (userRepository.existsByEmail(ADMIN_EMAIL)) {
                log.info("Admin user đã tồn tại với email: {}", ADMIN_EMAIL);
                return;
            }

            // Lấy role ADMIN
            Role adminRole = roleRepository.findByName(RoleName.ADMIN.name())
                    .orElseThrow(() -> new RuntimeException("Role ADMIN không tồn tại trong database"));

            // Tạo admin user
            User adminUser = User.builder()
                    .email(ADMIN_EMAIL)
                    .fullName(ADMIN_FULL_NAME)
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .role(adminRole)
                    .status(UserStatus.ACTIVE)
                    .emailVerified(true)
                    .build();

            userRepository.saveAndFlush(adminUser);
            log.info("Admin user được tạo thành công!");
            log.info("Email: {}", ADMIN_EMAIL);
            log.info("Password: {}", ADMIN_PASSWORD);
            
        } catch (Exception e) {
            log.error("Lỗi khi khởi tạo admin user: {}", e.getMessage(), e);
        }
    }

    private void initializeNormalUser() {
        try {
            // Kiểm tra xem user đã tồn tại chưa
            if (userRepository.existsByEmail(USER_EMAIL)) {
                log.info("Normal user đã tồn tại với email: {}", USER_EMAIL);
                return;
            }

            // Lấy role USER
            Role userRole = roleRepository.findByName(RoleName.USER.name())
                    .orElseThrow(() -> new RuntimeException("Role USER không tồn tại trong database"));

            // Tạo normal user
            User normalUser = User.builder()
                    .email(USER_EMAIL)
                    .fullName(USER_FULL_NAME)
                    .password(passwordEncoder.encode(USER_PASSWORD))
                    .role(userRole)
                    .status(UserStatus.ACTIVE)
                    .emailVerified(true)
                    .build();

            userRepository.saveAndFlush(normalUser);
            log.info("Normal user được tạo thành công!");
            log.info("Email: {}", USER_EMAIL);
            log.info("Password: {}", USER_PASSWORD);
            
        } catch (Exception e) {
            log.error("Lỗi khi khởi tạo normal user: {}", e.getMessage(), e);
        }
    }

    private void initializeRecruiterUser() {
        try {
            // Kiểm tra xem user recruiter đã tồn tại chưa
            if (userRepository.existsByEmail(RECRUITER_EMAIL)) {
                log.info("Recruiter user đã tồn tại với email: {}", RECRUITER_EMAIL);
                return;
            }

            // Lấy role RECRUITER
            Role recruiterRole = roleRepository.findByName(RoleName.RECRUITER.name())
                    .orElseThrow(() -> new RuntimeException("Role RECRUITER không tồn tại trong database"));

            // Tạo recruiter user
            User recruiterUser = User.builder()
                    .email(RECRUITER_EMAIL)
                    .fullName(RECRUITER_FULL_NAME)
                    .password(passwordEncoder.encode(RECRUITER_PASSWORD))
                    .role(recruiterRole)
                    .status(UserStatus.ACTIVE)
                    .emailVerified(true)
                    .build();

            userRepository.saveAndFlush(recruiterUser);
            log.info("Recruiter user được tạo thành công!");
            log.info("Email: {}", RECRUITER_EMAIL);
            log.info("Password: {}", RECRUITER_PASSWORD);
            
        } catch (Exception e) {
            log.error("Lỗi khi khởi tạo recruiter user: {}", e.getMessage(), e);
        }
    }
}
