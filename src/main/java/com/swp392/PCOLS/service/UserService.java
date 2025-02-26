package com.swp392.PCOLS.service;

import com.swp392.PCOLS.dto.RegisterDTO;
import com.swp392.PCOLS.entity.User;

import java.util.List;

public interface UserService {
    User register(String username, String rawPassword, String email, String address, String phone, int roleId);

    List<User> getAllUsers(String keyword, String sortBy);

    User getUserById(Long id);

    void updatePassword(String name, String currentPassword, String newPassword) throws Exception;

    public String registerv2(RegisterDTO registerDTO);

    String verifyAccount(String email, String otp);
}
