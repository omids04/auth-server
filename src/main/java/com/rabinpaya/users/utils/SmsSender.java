package com.rabinpaya.users.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SmsSender {

    private final String smsServiceUrl;
    private final WebClient client;

    private final String msgTemplate = """
            {
              "from": "50004001018639",
              "to": "%s",
              "text": "%s"
            }
            """;

    public SmsSender(@Value("${sms.path}") String smsServiceUrl) {
        this.smsServiceUrl = smsServiceUrl;
        this.client = WebClient.create();
    }

    public void send(String phone, String message){
       log.debug("sending message - {} - to phone number {}", message, phone);
       var uriSpec = client.post();
       var bodySpec = uriSpec.uri(smsServiceUrl);
       var headersSpec = bodySpec.bodyValue(msgTemplate.formatted(phone, message));
       headersSpec.header("content-type", MediaType.APPLICATION_JSON_VALUE);
       headersSpec.exchangeToMono(res -> {
           if(res.statusCode().is2xxSuccessful())
               return Mono.just(true);
           log.error("problem in sending sms. code is {} and body is {}", res.statusCode(), res.bodyToMono(String.class).block());
           return Mono.just(false);
       });
    }
}
