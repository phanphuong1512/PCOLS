package com.swp392.PCOLS.service.impl;

import com.swp392.PCOLS.entity.User;
import com.swp392.PCOLS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository UserRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository UserRepository) {
        this.UserRepository = UserRepository;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = this.UserRepository.findByUsername(username);
        return opt.orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
