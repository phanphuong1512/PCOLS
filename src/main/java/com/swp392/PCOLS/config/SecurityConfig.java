package com.swp392.PCOLS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
            .formLogin(login -> {
                login.loginPage("/auth/login");
                login.usernameParameter("usr");
                login.passwordParameter("pwd");
                login.defaultSuccessUrl("/home");
                login.failureUrl("/auth/login?error");
            })
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/auth/login").permitAll();
                auth.anyRequest().authenticated();
        });
        return http.build();
    }

}
