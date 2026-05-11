package com.example.demo.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpCookie;

@Getter
@AllArgsConstructor
public class AuthResult {
    private AuthTokenResponse authTokenResponse;
    private HttpCookie httpCookie;
}
