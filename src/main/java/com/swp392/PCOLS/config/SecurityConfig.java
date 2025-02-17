package com.swp392.PCOLS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for defining security rules and beans.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Configures security filters for HTTP requests.
     *
     * @param http HttpSecurity object to configure security settings.
     * @return SecurityFilterChain configured with specified security rules.
     * @throws Exception If configuration encounters an error.
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .formLogin(login -> login
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/home")
                        .failureUrl("/auth/login?error")
                        .permitAll()
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/scss/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
    /**
     * Provides an instance of BCryptPasswordEncoder for password encoding.
     *
     * @return BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
