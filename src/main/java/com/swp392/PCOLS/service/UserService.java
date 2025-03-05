package com.swp392.PCOLS.service;

import com.swp392.PCOLS.dto.LoginDTO;
import com.swp392.PCOLS.dto.RegisterDTO;

public interface UserService {

    String login(LoginDTO loginDTO);

    void register(RegisterDTO registerDTO);

    void verifyAccount(String email, String otp);

    void regenerateOtp(String email);

    void updatePassword(String name, String currentPassword, String newPassword) throws Exception;

    void forgotPassword(String email);

    void resetPassword(String email, String newPassword);
}
