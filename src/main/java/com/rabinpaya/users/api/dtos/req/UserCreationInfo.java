package com.rabinpaya.users.api.dtos.req;

import com.rabinpaya.users.model.User;

public record UserCreationInfo(String phone, String otp, String name) {
    public User toEntity() {
        var user = new User();
        user.setPhone(phone);
        return user;
    }
}
