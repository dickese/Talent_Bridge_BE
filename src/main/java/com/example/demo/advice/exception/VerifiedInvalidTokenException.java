package com.example.demo.advice.exception;

import com.example.demo.model.persistence.emailToken.VerifiedErrorTokenResult;

public class VerifiedInvalidTokenException extends RuntimeException {
    public VerifiedInvalidTokenException(VerifiedErrorTokenResult err) {
        super(err.getMessage());
    }
}
