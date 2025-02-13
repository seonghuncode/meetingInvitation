package com.dnd12.meetinginvitation.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 만료시간 설정 -> 자동 삭제
     */
    @Transactional
    public void setValuesWithTimeout(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 키 삭제
     */
    @Transactional
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 키를 이용한 값 확인
     */
    public Object getValues(String key) {
        return redisTemplate.opsForValue().get(key);
    }


}
