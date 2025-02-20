package com.swp392.PCOLS.service.impl;

import com.swp392.PCOLS.entity.User;
import com.swp392.PCOLS.repository.UserRepository;
import com.swp392.PCOLS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Updates the password for a given user if the current password is correct.
     *
     * @param username        The username of the user.
     * @param currentPassword The current password of the user.
     * @param newPassword     The new password to be set.
     * @throws Exception if the current password is incorrect or the user is not found.
     */
    public void updatePassword(String username, String currentPassword, String newPassword) throws Exception {
        // Find the user by username or throw an exception if not found
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Check if the current password matches
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new Exception("Current password is incorrect");
        }

        // Encode and set the new password, then save the user
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Checks if a user exists by username.
     *
     * @param username The username to check.
     * @return true if the user exists, false otherwise.
     */
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Saves a user to the repository.
     *
     * @param user The user to save.
     */
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User registerNewUser(String username, String rawPassword, String email, String address, String phone, int roleId) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(rawPassword)); //Encode password
        newUser.setEmail(email);

        // Nếu form không gửi address, phone thì gán giá trị mặc định
        newUser.setAddress(address != null ? address : "");
        newUser.setPhone(phone != null ? phone : "");

        newUser.setRoleId(roleId); // Bạn có thể gán role mặc định (ví dụ: 1 cho user bình thường)
        newUser.setStatus(true); // Đánh dấu tài khoản là enabled

        return userRepository.save(newUser);
    }

    @Override
    public List<User> getAllUsers(String keyword, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.findByUsernameContainingIgnoreCase(keyword, sort);
        }
        return userRepository.findAll(sort);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
