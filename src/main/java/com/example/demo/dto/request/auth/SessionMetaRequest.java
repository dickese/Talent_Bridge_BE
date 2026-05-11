package com.example.demo.dto.request.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionMetaRequest {
    private String deviceName;
    private String deviceType;
    private String userAgent;
}
