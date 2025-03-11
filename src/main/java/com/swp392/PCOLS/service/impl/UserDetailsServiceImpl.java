package com.swp392.PCOLS.service.impl;

import com.swp392.PCOLS.entity.User;
import com.swp392.PCOLS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> name = this.userRepository.findByUsername(username);
        return name.orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
    }
}