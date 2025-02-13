package com.swp392.PCOLS.service.impl;

import com.swp392.PCOLS.entity.User;
import com.swp392.PCOLS.repository.UserRepository;
import com.swp392.PCOLS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerNewUser(String username, String rawPassword, String email,
                                String address, String phone, int roleId) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(rawPassword)); // Mã hóa mật khẩu
        newUser.setEmail(email);

        // Nếu form không gửi address, phone thì gán giá trị mặc định
        newUser.setAddress(address != null ? address : "");
        newUser.setPhone(phone != null ? phone : "");

        newUser.setRoleId(roleId); // Bạn có thể gán role mặc định (ví dụ: 1 cho user bình thường)
        newUser.setStatus(true); // Đánh dấu tài khoản là enabled

        return userRepository.save(newUser);
    }
}
