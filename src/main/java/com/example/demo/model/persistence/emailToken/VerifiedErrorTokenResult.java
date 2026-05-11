package com.example.demo.model.persistence.emailToken;

import lombok.Getter;

@Getter
public enum VerifiedErrorTokenResult {
    TOKEN_EXPIRED("Token is expired"),
    INVALID_TOKEN("Token is invalid")
    ;

    VerifiedErrorTokenResult(String message) {
        this.message = message;
    }

    private final String message;
}
