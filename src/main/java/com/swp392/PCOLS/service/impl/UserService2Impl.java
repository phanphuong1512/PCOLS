package com.swp392.PCOLS.service.impl;

import com.swp392.PCOLS.dto.RegisterDTO;
import com.swp392.PCOLS.entity.User;
import com.swp392.PCOLS.repository.UserRepository;
import com.swp392.PCOLS.service.UserService;
import com.swp392.PCOLS.util.EmailUtil;
import com.swp392.PCOLS.util.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Primary
public class UserService2Impl implements UserService {

    private final OtpUtil otpUtil;
    private final EmailUtil emailUtil;
    private final UserRepository userRepository;

    @Override
    public String registerv2(RegisterDTO registerDTO) {
        String otp = otpUtil.generateOTP();
        try {
            emailUtil.sendOtpEmail(registerDTO.email(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email otp");
        }
        User user = new User();
        user.setUsername(registerDTO.username());
        user.setPassword(registerDTO.password());
        user.setEmail(registerDTO.email());
        user.setOtp(otp);
        user.setExpirationTime(LocalDateTime.now());
        userRepository.save(user);
        return "User Registration Successful";
    }

    @Override
    public String verifyAccount(String email, String otp) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> (new RuntimeException("User not found" + email)));
        if (user.getOtp().equals(otp) && Duration
                .between(user.getExpirationTime(), LocalDateTime.now()).getSeconds() <= 300) {
            user.setStatus(true);
            userRepository.save(user);
            return "Account verified";

        }
        return null;
    }

    @Override
    public User register(String username, String rawPassword, String email, String address, String phone, int roleId) {
        return null;
    }

    @Override
    public List<User> getAllUsers(String keyword, String sortBy) {
        return List.of();
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public void updatePassword(String name, String currentPassword, String newPassword) throws Exception {

    }

}
