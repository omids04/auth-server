package com.rabinpaya.users.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Profile("production")
@RequiredArgsConstructor
public class RedisOtpCache implements OtpCache{

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String phone, String otp) {
        redisTemplate.opsForValue().set(phone, otp, Duration.ofMinutes(2));
    }

    @Override
    public String get(String phone) {
        return redisTemplate.opsForValue().get(phone);
    }
}
