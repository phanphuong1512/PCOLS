package com.swp392.PCOLS.repository;

import com.swp392.PCOLS.entity.ForgotPassword;
import com.swp392.PCOLS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {

    @Query("SELECT f FROM ForgotPassword f WHERE f.user = ?1 AND f.otp = ?2")
    Optional<ForgotPassword> findByEmailAndOtp(User user, Integer otp);
}
