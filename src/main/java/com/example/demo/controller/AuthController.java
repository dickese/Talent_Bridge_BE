package com.example.demo.controller;


import com.example.demo.dto.request.auth.LoginRequest;
import com.example.demo.dto.request.auth.UserRegisterRequest;
import com.example.demo.dto.request.auth.SessionMetaRequest;
import com.example.demo.dto.response.auth.AuthResult;
import com.example.demo.dto.response.auth.AuthTokenResponse;
import com.example.demo.dto.response.user.UserDetailsResponse;
import com.example.demo.dto.response.user.UserSessionResponse;
import com.example.demo.service.AuthService;
import io.swagger.v3.oas.annotations.headers.Header;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public void register(@RequestBody UserRegisterRequest request){
        authService.register(request);
    }

//    @PostMapping("/verify-email")
//    public ResponseEntity<ApiResponse<?>> verify(@RequestBody String token){
//        var verifiedResult = authService.verify(token);
//        return ResponseEntity.ok(ApiResponse.builder()
//                .success(true)
//                .data(verifiedResult.getEmail())
//                .build());
//    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenResponse> login(@RequestBody LoginRequest request){
        AuthResult authenticationResult = authService.login(request);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, authenticationResult.getHttpCookie().toString())
                .body(authenticationResult.getAuthTokenResponse());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthTokenResponse> refreshToken(@CookieValue(name = "REFRESH_TOKEN") String refreshToken, @RequestBody SessionMetaRequest sessionRequest){
        System.out.println("Received refresh token: " + refreshToken);
        AuthResult authenticationResult = authService.refreshToken(refreshToken, sessionRequest);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, authenticationResult.getHttpCookie().toString())
                .body(authenticationResult.getAuthTokenResponse());
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "REFRESH_TOKEN") String refreshToken){
        ResponseCookie responseCookie = authService.handleLogout(refreshToken);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }


    @GetMapping("/me")
    public UserSessionResponse getCurrentUserSession(){
        return authService.getCurrentUserSession();
    }

    @GetMapping("/me/details")
    public UserDetailsResponse getCurrentUserDetails(){
        return authService.getCurrentUserDetails();
    }
}
