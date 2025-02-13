package com.swp392.PCOLS.service;

import com.swp392.PCOLS.entity.User;

public interface UserService {
    User registerNewUser(String username, String rawPassword, String email, String address, String phone, int roleId);
}
