package com.rabinpaya.users.api.controllers;

import com.rabinpaya.users.api.dtos.req.PhoneNumber;
import com.rabinpaya.users.api.dtos.req.UserCreationInfo;
import com.rabinpaya.users.api.dtos.res.UserInfo;
import com.rabinpaya.users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/code")
    @ResponseStatus(HttpStatus.OK)
    public void code(@RequestBody PhoneNumber number){
        userService.sendOtpTo(number.phone());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserInfo newUser(@RequestBody UserCreationInfo model){
        var user = model.toEntity();
        var savedUser = userService.registerUser(user);
        return UserInfo.fromEntity(savedUser);
    }
}
