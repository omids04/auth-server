package com.rabinpaya.users.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class InMemoryCache implements OtpCache{
    private final Map<String, String> cache = new HashMap<>();
    @Override
    public void save(String phone, String otp) {
        cache.put(phone, otp);
    }
    @Override
    public String get(String phone) {
        return cache.get(phone);
    }
}
