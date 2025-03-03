package com.swp392.PCOLS.web.mvc;

import com.swp392.PCOLS.dto.ChangePasswordDTO;
import com.swp392.PCOLS.dto.EmailBodyDTO;
import com.swp392.PCOLS.entity.ForgotPassword;
import com.swp392.PCOLS.entity.User;
import com.swp392.PCOLS.repository.ForgotPasswordRepository;
import com.swp392.PCOLS.repository.UserRepository;
import com.swp392.PCOLS.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordController(UserRepository userRepository, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/verify-email/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        int otp = otpGenerator();
        EmailBodyDTO emailBodyDTO = EmailBodyDTO.builder()
                .to(email)
                .subject("PCOLS - Password Reset")
                .body("This is the OTP" + otp)
                .build();
        ForgotPassword forgotPassword = ForgotPassword.builder()
                .otp(otp)
                .user(user)
                .expirationTime(new Date(System.currentTimeMillis() + 300_000))
                .build();
        emailService.sendSimpleMail(emailBodyDTO);
        forgotPasswordRepository.save(forgotPassword);

        return ResponseEntity.ok("Email sent for the verification");

    }

    @PostMapping("/verify-otp/{email}/{otp}")
    public ResponseEntity<String> verifyOTP(@PathVariable String email, @PathVariable int otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
        ForgotPassword forgotPassword = forgotPasswordRepository.findByEmailAndOtp(user, otp)
                .orElseThrow(() -> new RuntimeException("OTP not found" + email));
        if (forgotPassword.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(forgotPassword.getId());
            return new ResponseEntity<>("OTP expired", HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok("OTP verified");
    }

    @PostMapping("/reset-password/{email}")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO,
                                                 @PathVariable String email) {
        if (!Objects.equals(changePasswordDTO.password(), changePasswordDTO.confirmPassword())) {
            return new ResponseEntity<>("Passwords do not match", HttpStatus.EXPECTATION_FAILED);
        }
        String encodedPassword = passwordEncoder.encode(changePasswordDTO.password());
        userRepository.updatePassword(email, encodedPassword);
        return ResponseEntity.ok("Password has updated successfully");
    }


    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);

    }
}

