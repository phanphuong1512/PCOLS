package com.swp392.PCOLS.repository;

import com.swp392.PCOLS.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByUsernameContainingIgnoreCase(String name, Sort sort);

    Optional<User> findByEmail(String email);

}
