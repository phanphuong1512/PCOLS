package com.swp392.PCOLS.service;

import com.swp392.PCOLS.entity.User;

import java.util.List;

public interface UserService {
    User registerNewUser(String username, String rawPassword, String email, String address, String phone, int roleId);
    List<User> getAllUsers(String keyword, String sortBy);
    User getUserById(Long id);
}
