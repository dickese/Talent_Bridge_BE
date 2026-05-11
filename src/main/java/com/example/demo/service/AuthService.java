package com.example.demo.service;

import com.example.demo.dto.request.auth.LoginRequest;
import com.example.demo.dto.request.auth.UserLoginRequest;
import com.example.demo.dto.request.auth.UserRegisterRequest;
import com.example.demo.dto.request.auth.SessionMetaRequest;
import com.example.demo.dto.response.auth.AuthResult;
import com.example.demo.dto.response.auth.VerifiedEmailTokenResponse;
import com.example.demo.dto.response.user.UserDetailsResponse;
import com.example.demo.dto.response.user.UserSessionResponse;
import com.example.demo.model.domain.user.User;
import com.example.demo.model.persistence.RefreshToken;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;

public interface AuthService {
    void register(UserRegisterRequest request);
    VerifiedEmailTokenResponse verify(String token);
    AuthResult login(LoginRequest request);
    AuthResult refreshToken(String refreshToken, SessionMetaRequest session);
    ResponseCookie handleLogout(String refreshToken);
    User getCurrentUser();
    UserSessionResponse getCurrentUserSession();
    UserDetailsResponse getCurrentUserDetails();
}
