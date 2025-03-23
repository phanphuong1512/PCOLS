package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.dto.LoginDTO;
import fpt.swp.pcols.dto.RegisterDTO;
import fpt.swp.pcols.entity.User;
import fpt.swp.pcols.repository.UserRepository;
import fpt.swp.pcols.service.UserService;
import fpt.swp.pcols.util.EmailUtil;
import fpt.swp.pcols.util.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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
        User user = userRepository.findByEmail(email).orElseThrow(() -> (new RuntimeException("Email not found " + email + "oh no")));
        if (user.getOtp().equals(otp) && Duration
                .between(user.getExpirationTime(), LocalDateTime.now()).getSeconds() <= 300) {
            user.setEnabled(true);
            userRepository.save(user);
        } else {
            throw new RuntimeException("OTP incorrect or expired. Please regenerate OTP and try again.");
        }
    }

    @Override
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
        } else if (!user.isEnabled()) {
            return "Account not verified";
        }
        return "Login successful";
    }

    public void updatePassword(String username, String currentPassword, String newPassword, String confirmNewPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from the current password");
        }
        if (!newPassword.equals(confirmNewPassword)) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


    @Override
    public void forgotPassword(String email) {
        userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        try {
            emailUtil.sendSetPassword(email);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email otp", e);
        }
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
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
    public Optional<User> findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    public void updateUser(User updatedUser) {
        // Tìm user hiện tại trong cơ sở dữ liệu
        User existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + updatedUser.getId()));

        // Cập nhật các trường từ updatedUser
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setEnabled(true);

        userRepository.save(existingUser);
    }

    public boolean checkPassword(String username, String rawPassword) {
        Optional<User> userOptional = findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return passwordEncoder.matches(rawPassword, user.getPassword()); // So sánh mật khẩu mã hóa
        }
        return false;
    }

    public void changePassword(String username, String newPassword) {
        Optional<User> userOptional = findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword)); // Mã hóa mật khẩu mới
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
