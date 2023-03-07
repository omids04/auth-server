package com.rabinpaya.users.cache;

public interface OtpCache {
    void save(String phone, String otp);
    String get(String phone);
}
