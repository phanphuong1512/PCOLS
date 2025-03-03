package com.swp392.PCOLS.service;

import com.swp392.PCOLS.dto.LoginDTO;
import com.swp392.PCOLS.dto.RegisterDTO;

public interface UserService {

    String login(LoginDTO loginDTO);

    public String register(RegisterDTO registerDTO);

    String verifyAccount(String email, String otp);

    String regenerateOtp(String email);

    void updatePassword(String name, String currentPassword, String newPassword) throws Exception;


}
