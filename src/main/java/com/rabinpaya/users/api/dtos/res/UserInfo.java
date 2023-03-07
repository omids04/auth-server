package com.rabinpaya.users.api.dtos.res;

import com.rabinpaya.users.model.User;

import java.time.Instant;
import java.util.UUID;

public record UserInfo(UUID id, String username, Instant creationTimestamp) {
    public static UserInfo fromEntity(User user) {
        return new UserInfo(user.getId(), user.getPassword(), user.getCreationTimestamp());
    }
}
