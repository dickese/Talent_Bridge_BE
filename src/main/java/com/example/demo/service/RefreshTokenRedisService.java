package com.example.demo.service;

import com.example.demo.dto.request.auth.SessionMetaRequest;
import com.example.demo.dto.response.auth.SessionMetaResponse;

import java.time.Duration;
import java.util.List;

public interface RefreshTokenRedisService {
    void saveRefreshToken(String token, String userId, SessionMetaRequest sessionMetaRequest, Duration expire);

    boolean hasToken(String token, String userId);

    void deleteRefreshToken(String token, String userId);

    void deleteRefreshToken(String key);

    List<SessionMetaResponse> getAllSessionMetaByUserId(String userId);
}
