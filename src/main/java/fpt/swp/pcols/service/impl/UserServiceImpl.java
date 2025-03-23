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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


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
        user.setAvatar("https://engineering.usask.ca/images/no_avatar.jpg");
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

    @Transactional
    public void updateUser(User updatedUser) {
        logger.debug("Starting updateUser for user with id: {}", updatedUser.getId());

        // Tìm user hiện tại trong cơ sở dữ liệu
        User existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + updatedUser.getId()));

        logger.debug("Current user state: {}", existingUser);

        // Cập nhật các trường từ updatedUser
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setAvatar(updatedUser.getAvatar()); // Thêm để cập nhật avatar

        // Chỉ đặt enabled = true nếu cần thiết (bỏ nếu không cần)
        // existingUser.setEnabled(true);

        logger.debug("User state after update: {}", existingUser);

        // Lưu thay đổi
        userRepository.save(existingUser);
        logger.info("User updated successfully with id: {}", existingUser.getId());
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

    @Override
    public List<User> searchUsers(String role, String email) {
        if ((role == null || role.isEmpty()) && (email == null || email.isEmpty())) {
            return userRepository.findAll();
        } else if (role != null && !role.isEmpty() && (email == null || email.isEmpty())) {
            return userRepository.findByAuthorities_Authority(role);
        } else if ((role == null || role.isEmpty()) && email != null && !email.isEmpty()) {
            return userRepository.findByEmailContaining(email);
        } else {
            return userRepository.findByAuthorities_AuthorityAndEmailContaining(role, email);
        }
    }
}