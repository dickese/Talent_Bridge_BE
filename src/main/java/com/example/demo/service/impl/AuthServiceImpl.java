package com.example.demo.service.impl;

import com.example.demo.advice.exception.UnauthorizedException;
import com.example.demo.advice.exception.VerifiedInvalidTokenException;
import com.example.demo.advice.exception.VerifiedTokenExpiredException;
import com.example.demo.config.auth.AuthConfig;
import com.example.demo.dto.request.auth.LoginRequest;
import com.example.demo.dto.request.auth.UserRegisterRequest;
import com.example.demo.dto.request.auth.SessionMetaRequest;
import com.example.demo.dto.response.auth.AuthResult;
import com.example.demo.dto.response.auth.AuthTokenResponse;
import com.example.demo.dto.response.auth.VerifiedEmailTokenResponse;
import com.example.demo.dto.response.user.UserDetailsResponse;
import com.example.demo.dto.response.user.UserSessionResponse;
import com.example.demo.model.persistence.RefreshToken;
import com.example.demo.model.persistence.emailToken.EmailVerificationToken;
import com.example.demo.model.domain.user.Role;
import com.example.demo.model.domain.user.User;
import com.example.demo.model.domain.user.RoleName;
import com.example.demo.model.domain.user.UserStatus;
import com.example.demo.model.persistence.emailToken.VerifiedErrorTokenResult;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.EmailService;
import com.example.demo.service.RefreshTokenRedisService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final String FRONTEND_URL = "http://localhost:5173/academy";
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final RefreshTokenRedisService refreshTokenRedisService;
    private final AuthConfig authConfig;

    @Value("${jwt.access-token.expiration}")
    public Long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    public Long refreshTokenExpiration;

    @Override
    public VerifiedEmailTokenResponse verify(String token) {
        EmailVerificationToken tokenEntity = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new VerifiedInvalidTokenException(VerifiedErrorTokenResult.INVALID_TOKEN));

        var user = tokenEntity.getUser();
        if(tokenEntity.isExpired()){
            throw new VerifiedTokenExpiredException(VerifiedErrorTokenResult.TOKEN_EXPIRED);
        }
        else if(tokenEntity.isUsed()){
            return VerifiedEmailTokenResponse.already_verified(user.getEmail());
        }

        tokenEntity.use();
        user.verifyEmail();
        user.activate();

        return VerifiedEmailTokenResponse.success(user.getEmail());
    }

    @Override
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public void register(UserRegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new DataIntegrityViolationException("Email is already exist");
        }

        User mappedUser = mapUserRegisterRequestToUser(request);
        Role userRole;
        if(request.isRecruiter()){
            userRole = roleRepository.findByName(RoleName.RECRUITER.name())
                    .orElseThrow(() -> new EntityNotFoundException("Can't find role: " + RoleName.RECRUITER.name()));
        }
        else {
            userRole = roleRepository.findByName(RoleName.USER.name())
                    .orElseThrow(() -> new EntityNotFoundException("Can't find role " + RoleName.USER.name()));
        }

        String hashPassword = authConfig.passwordEncoder().encode(mappedUser.getPassword());

        mappedUser.setPassword(hashPassword);
        mappedUser.setRole(userRole);
        mappedUser.setStatus(UserStatus.ACTIVE);

        userRepository.save(mappedUser);
    }

    public String generateVerifyEmailUrl(User user) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken tokenEntity = EmailVerificationToken.builder()
                .user(user)
                .token(token)
                .expiredAt(Instant.now().plus(15, ChronoUnit.MINUTES)).build();

        verificationTokenRepository.save(tokenEntity);
        return FRONTEND_URL + "/auth/verify-email&token=" + token;
    }

    @Override
    public AuthResult login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequest.getCredentials().getEmail(),
                loginRequest.getCredentials().getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        System.out.println(principal.toString());

        //userName  = email
        return buildAuthResult(principal.getUsername(),loginRequest.getSession());
    }

    @Override
    @Transactional
    public AuthResult refreshToken(String refreshToken, SessionMetaRequest session) {
        Long userId = validateRefreshToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Can't find user with id " + userId));

        refreshTokenRedisService.deleteRefreshToken(refreshToken, userId.toString());

        return buildAuthResult(user.getEmail(),session);
    }

    @Override
    public ResponseCookie handleLogout(String refreshToken) {
        Long userId = validateRefreshToken(refreshToken);
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find user with id " + userId));

        refreshTokenRedisService.deleteRefreshToken(refreshToken, user.getId().toString());

        return ResponseCookie
                .from("REFRESH_TOKEN", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();
    }

    @Override
    public UserSessionResponse getCurrentUserSession() {
        User currentUser = getCurrentUser();
        return mapUserToUserSessionResponse(currentUser);
    }

    @Override
    public UserDetailsResponse getCurrentUserDetails() {
        User currentUser = getCurrentUser();
        return UserDetailsResponse.builder()
                .id(currentUser.getId())
                .name(currentUser.getFullName())
                .email(currentUser.getEmail())
                .avatarUrl(currentUser.getAvatarUrl())
                .gender(currentUser.getGender())
                .dob(currentUser.getDob())
                .address(currentUser.getAddress())
                .createdAt(currentUser.getCreatedAt())
                .updatedAt(currentUser.getUpdatedAt())
                .build();
    }

    private Long validateRefreshToken(String refreshToken) {
        Jwt jwt;
        try {
            jwt = jwtDecoder.decode(refreshToken);
        } catch (JwtException e) {
            throw new UnauthorizedException("Invalid refresh token or token expired");
        }

        log.info("Decoded JWT: {}", jwt);

        String tokenType = jwt.getClaimAsString("typ");
        if (!"refresh".equals(tokenType)) {
            throw new UnauthorizedException("Invalid token by refresh");
        }

        Long userId = Long.parseLong(jwt.getSubject());
        if (!refreshTokenRedisService.hasToken(refreshToken, Long.toString(userId))) {
            throw new UnauthorizedException("Invalid token by id");
        }

        return userId;
    }


    private AuthResult buildAuthResult(String email, SessionMetaRequest session) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Can't find user with email"));

        String accessToken = buildAccessToken(accessTokenExpiration, user);
        AuthTokenResponse authToken = AuthTokenResponse.builder()
                .accessToken(accessToken)
                .userSessionResponse(mapUserToUserSessionResponse(user))
                .build();


        String refreshToken = buildRefreshToken(refreshTokenExpiration, user);
        refreshTokenRedisService.saveRefreshToken(
                refreshToken,
                user.getId().toString(),
                session,
                Duration.ofSeconds(refreshTokenExpiration));

        ResponseCookie responseCookie = ResponseCookie
                .from("REFRESH_TOKEN",refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(refreshTokenExpiration)
                .build();

        return new AuthResult(authToken, responseCookie);
    }

    public User mapUserRegisterRequestToUser(UserRegisterRequest request){
        return User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(request.getPassword())
                .dob(request.getDob())
                .address(request.getAddress())
                .build();
    }


    private String buildAccessToken(Long expirationRate, User user) {
        Instant now = Instant.now();
        Instant validity = now.plus(expirationRate, ChronoUnit.SECONDS);

        // Debug logging
        System.out.println("=== JWT CREATION DEBUG ===");
        System.out.println("Current time (UTC): " + now);
        System.out.println("Current time (System): " + now.atZone(ZoneId.systemDefault()));
        System.out.println("Token validity: " + validity);
        System.out.println("Token validity (System): " + validity.atZone(ZoneId.systemDefault()));
        System.out.println("Expiration in seconds: " + expirationRate);
        System.out.println("System timezone: " + ZoneId.systemDefault());
        System.out.println("==========================");

        // Header
        JwsHeader jwsHeader = JwsHeader.with(AuthConfig.MAC_ALGORITHM).build();

        // Payload
        String jti = UUID.randomUUID().toString();
        Role role = user.getRole();
        List<String> permissions = role != null && role.getPermissions() != null
                ? role.getPermissions().stream().map(p -> p.getMethod() + " " + p.getApiPath()).toList()
                : List.of();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .id(jti)
                .issuedAt(now)
                .expiresAt(validity)
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", role.getName())
                .claim("permissions", permissions)
                .claim("typ", "access")
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }


    private String buildRefreshToken(Long expirationRate, User user) {
        Instant now = Instant.now();
        Instant validity = now.plus(expirationRate, ChronoUnit.SECONDS);
        JwsHeader jwsHeader = JwsHeader.with(AuthConfig.MAC_ALGORITHM).build();
        String jti = UUID.randomUUID().toString();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .id(jti)
                .issuedAt(now)
                .expiresAt(validity)
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("typ","refresh")
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    private UserSessionResponse mapUserToUserSessionResponse(User user){
        Role role = user.getRole();
        List<String> permissions = role != null && role.getPermissions() != null
                ? role.getPermissions().stream().map(p -> p.getMethod() + " " + p.getApiPath()).toList()
                : List.of();
        return UserSessionResponse.builder()
                .id(user.getId())
                .name(user.getFullName())
                .email(user.getEmail())
                .status(user.getStatus().toString())
                .avatarUrl(user.getAvatarUrl())
                .verifiedEmail(user.isEmailVerified())
                .role(role.getName().toString())
                .permissions(permissions)
                .companyId(user.getCompany() != null ? user.getCompany().getId() : null)
                .build();
    }

}
