package com.example.king.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public void saveRefreshToken(Long userId, String refreshToken, Duration duration){

        String key = "refreshToken:user:"+userId;
        redisTemplate.opsForValue()
                .set(key, refreshToken, duration);
    }

    public String getRefreshToken(Long userId) {
        String key = "refreshToken:user:" + userId;
        return redisTemplate.opsForValue()
                .get(key);
    }

    public void deleteRefreshToken(Long userId) {
        String key = "refreshToken:user:" + userId;
        redisTemplate.delete(key);
    }

}
