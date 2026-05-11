package com.example.demo.dto.response.auth;

import com.example.demo.dto.response.user.UserSessionResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@JsonPropertyOrder({"user", "accessToken"})
public class AuthTokenResponse {
    @JsonProperty("user")
    private UserSessionResponse userSessionResponse;
    private String accessToken;
}
