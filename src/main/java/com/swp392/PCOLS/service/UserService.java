package com.swp392.PCOLS.service;

import com.swp392.PCOLS.dto.LoginDTO;
import com.swp392.PCOLS.dto.RegisterDTO;
import com.swp392.PCOLS.entity.User;

import java.util.List;

public interface UserService {

    String login(LoginDTO loginDTO);

    void register(RegisterDTO registerDTO);

    void verifyAccount(String email, String otp);

    void regenerateOtp(String email);

    void updatePassword(String name, String currentPassword, String newPassword,String confirmNewPassword) throws Exception;

    void forgotPassword(String email);
    List<User> getAllUser();

    void resetPassword(String email, String newPassword);
    User getUserById(Long id);

    void updateUser(User user);
}
