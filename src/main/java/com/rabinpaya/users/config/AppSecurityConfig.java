package com.rabinpaya.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AppSecurityConfig {

    @Bean
    @Order(2)
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .and()
                .authorizeHttpRequests().requestMatchers("/user/code").permitAll()
                .and()
                .authorizeHttpRequests().requestMatchers("/user").permitAll()
                .and()
                .authorizeHttpRequests().anyRequest().authenticated();
        return http.build();
    }

}
