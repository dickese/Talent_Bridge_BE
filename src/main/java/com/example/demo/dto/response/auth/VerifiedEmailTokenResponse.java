package com.example.demo.dto.response.auth;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VerifiedEmailTokenResponse {
    private String message;
    private String email;

    public static VerifiedEmailTokenResponse success(String email){
        return VerifiedEmailTokenResponse.builder()
                .message("Đã xác thực email")
                .email(email)
                .build();
    }

    public static VerifiedEmailTokenResponse already_verified(String email){
        return VerifiedEmailTokenResponse.builder()
                .message("Email đã được xác thực")
                .email(email)
                .build();
    }


}
