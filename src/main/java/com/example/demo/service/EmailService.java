package com.example.demo.service;

import com.example.demo.model.domain.user.User;

public interface EmailService {
    void sendVerifyEmail(String to, String name, String verifyUrl);
}
