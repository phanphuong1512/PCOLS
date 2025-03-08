package com.swp392.PCOLS.service.impl;

import com.swp392.PCOLS.dto.LoginDTO;
import com.swp392.PCOLS.dto.RegisterDTO;
import com.swp392.PCOLS.entity.User;
import com.swp392.PCOLS.repository.UserRepository;
import com.swp392.PCOLS.service.UserService;
import com.swp392.PCOLS.util.EmailUtil;
import com.swp392.PCOLS.util.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Primary
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpUtil otpUtil;
    private final EmailUtil emailUtil;


    @Override
    public void register(RegisterDTO registerDTO) {
        String otp = otpUtil.generateOTP();
        try {
            emailUtil.sendOtpEmail(registerDTO.email(), otp);
        } catch (MessagingException e) {
            log.error("Error sending email: {}", e.getMessage());
            throw new RuntimeException("Error sending email otp", e);
        }
        User user = new User();
        user.setUsername(registerDTO.username());
        user.setPassword(passwordEncoder.encode(registerDTO.password()));
        user.setEmail(registerDTO.email());
        user.setOtp(otp);
        user.setExpirationTime(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void verifyAccount(String email, String otp) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> (new RuntimeException("Email not found " + email + "Anhxinh")));
        if (user.getOtp().equals(otp) && Duration
                .between(user.getExpirationTime(), LocalDateTime.now()).getSeconds() <= 300) {
            user.setStatus(true);
            userRepository.save(user);
        } else {
            throw new RuntimeException("OTP incorrect or expired. Please regenerate OTP and try again.");
        }
    }

    public void regenerateOtp(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found" + email));
        String otp = otpUtil.generateOTP();
        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email otp");
        }
        user.setOtp(otp);
        user.setExpirationTime(LocalDateTime.now());
        userRepository.save(user);
    }

    public String login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.email()).orElseThrow(() -> new RuntimeException("User not found" + loginDTO.email()));
        if (!loginDTO.password().equals(user.getPassword())) {
            return "Login failed";
        } else if (!user.isStatus()) {
            return "Account not verified";
        }
        return "Login successful";
    }

    /**
     * Updates the password for a given user if the current password is correct.
     *
     * @param username        The username of the user.
     * @param currentPassword The current password of the user.
     * @param newPassword     The new password to be set.
     * @throws Exception if the current password is incorrect or the user is not found.
     */
    public void updatePassword(String username, String currentPassword, String newPassword) throws Exception {

        // Find the user by username or throw an exception if not found
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Check if the current password matches
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new Exception("Current password is incorrect");
        }

        // Encode and set the new password, then save the user
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void updateUser(User user) {
        this.userRepository.save(user);
    }
}