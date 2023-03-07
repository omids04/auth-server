package com.rabinpaya.users;

import com.rabinpaya.users.utils.SmsSender;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;


@SpringBootTest
public class UserRegistrationTest {


    @MockBean
    SmsSender sender;

    @MockBean
    RedisTemplate<String, String> redisTemplate;

    @Captor
    ArgumentCaptor<String> otpCaptor;

    @Captor
    ArgumentCaptor<String> phoneCaptor;

    @Captor
    ArgumentCaptor<String> hashedOtpCaptor;

    WebTestClient testClient = WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:8080")
            .build();


}
