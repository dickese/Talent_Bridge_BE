package com.example.demo.service.impl;

import com.example.demo.dto.request.auth.SessionMetaRequest;
import com.example.demo.dto.response.auth.SessionMetaResponse;
import com.example.demo.model.SessionMeta;
import com.example.demo.service.RefreshTokenRedisService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RefreshTokenRedisServiceImpl implements RefreshTokenRedisService {

    private final RedisTemplate<String, Object> redisSessionMetaTemplate;
    private final StringRedisTemplate redisStringTemplate;

    private String buildKey(String token, String userId) {
        return "auth:refresh:" + userId + ":" + DigestUtils.sha256Hex(token);
    }

    @Override
    public void saveRefreshToken(String token, String userId, SessionMetaRequest sessionMetaRequest, Duration expire) {
        String sessionId = buildKey(token, userId);
        Object sessionMeta = new SessionMeta(
                sessionId,
                sessionMetaRequest.getDeviceName(),
                sessionMetaRequest.getDeviceType(),
                sessionMetaRequest.getUserAgent(),
                Instant.now()
        );
        redisSessionMetaTemplate.opsForValue().set(sessionId,sessionMeta, expire);
        redisStringTemplate.opsForSet().add("auth:user_sessions:" + userId, sessionId);
    }

    @Override
    public boolean hasToken(String token, String userId) {
        return redisSessionMetaTemplate.hasKey(buildKey(token, userId));
    }

    @Override
    public void deleteRefreshToken(String token, String userId) {
        redisSessionMetaTemplate.delete(buildKey(token, userId));
    }

    @Override
    public void deleteRefreshToken(String key) {
        redisSessionMetaTemplate.delete(key);
    }


    @Override
    public List<SessionMetaResponse> getAllSessionMetaByUserId(String userId) {
        Set<String> sessions = redisStringTemplate.opsForSet()
                .members("auth:user_sessions:" + userId);

        if (sessions == null || sessions.isEmpty()) {
            return List.of();
        }

        List<Object> sessionMetaData = redisSessionMetaTemplate
                .opsForValue()
                .multiGet(new ArrayList<>(sessions));

        return sessionMetaData.stream()
                .filter(meta -> meta != null)
                .map(meta -> (SessionMeta) meta)
                .map(meta -> new SessionMetaResponse(
                        meta.getSessionId(),
                        meta.getDeviceName(),
                        meta.getDeviceType(),
                        meta.getUserAgent(),
                        meta.getLoginAt()
                ))
                .toList();
    }
}
