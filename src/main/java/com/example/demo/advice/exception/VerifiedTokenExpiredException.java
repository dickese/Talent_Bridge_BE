package com.example.demo.advice.exception;

import com.example.demo.model.persistence.emailToken.VerifiedErrorTokenResult;

public class VerifiedTokenExpiredException extends RuntimeException {
    public VerifiedTokenExpiredException(VerifiedErrorTokenResult err) {
        super(err.getMessage());
    }
}
