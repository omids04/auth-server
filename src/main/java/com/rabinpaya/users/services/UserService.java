package com.rabinpaya.users.services;

import com.rabinpaya.users.cache.OtpCache;
import com.rabinpaya.users.model.User;
import com.rabinpaya.users.repositories.UserRepository;
import com.rabinpaya.users.utils.SmsSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SmsSender sender;

    private final OtpCache otpCache;

    /**
     * verify otp and persist user info
     * @param user info to be created
     * @return saved user
     */
    @Transactional
    public User registerUser(User user){
        //verify otp
        var otp = otpCache.get(user.getPhone());
        //Todo check for null otp
        var otpMatches = passwordEncoder.matches(user.getOtp(), otp);
        if (!otpMatches)
            throw new RuntimeException();

        //Hashing user password
        var hashedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPass);

        //generate id for new user
        user.setId(UUID.randomUUID());

        return userRepository.save(user);
    }

    /**
     * generates an otp and sends it to provided phone number
     * also saves otp and phone in cache
     * @param phone number that going to receive otp
     */
    @Transactional
    public void sendOtpTo(String phone) {
        var otp = generateOtp();

        //save in cache
        otpCache.save(phone, otp);
        sendOtp(phone, otp);
    }

    private String generateOtp(){
        var num = (int)(10000 + Math.random() * 10000);
        return String.valueOf(num).substring(1, 5);
    }

    private void sendOtp(String phone, String otp){
        var msg = "your verification code is: " + otp;
        sender.send(phone, msg);
    }
}
