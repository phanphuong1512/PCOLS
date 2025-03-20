package fpt.swp.pcols.service;

import fpt.swp.pcols.dto.LoginDTO;
import fpt.swp.pcols.dto.RegisterDTO;
import fpt.swp.pcols.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    String login(LoginDTO loginDTO);

    void register(RegisterDTO registerDTO);

    void verifyAccount(String email, String otp);

    void regenerateOtp(String email);

    void updatePassword(String name, String currentPassword, String newPassword, String confirmNewPassword) throws Exception;

    void forgotPassword(String email);

    List<User> getAllUser();

    void resetPassword(String email, String newPassword);

    User getUserById(Long id);

    Optional<User> findByUsername(String name);

    Optional<User> findByEmail(String name);

    User save(User user);

}
