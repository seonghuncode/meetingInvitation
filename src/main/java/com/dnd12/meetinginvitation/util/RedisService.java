package com.dnd12.meetinginvitation.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public void getAllKeysWithTTL() {
        Set<String> keys = redisTemplate.keys("*");  // 모든 키 가져오기

        for (String key : keys) {
            String value = redisTemplate.opsForValue().get(key);
            Long ttl = redisTemplate.getExpire(key);  // TTL 가져오기
            log.info("Key: {}, Value: {}, TTL: {} seconds", key, value, ttl);
        }
    }
}