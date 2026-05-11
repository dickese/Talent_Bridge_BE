package com.example.demo.service.impl;

import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        System.out.println("LOAD USER: " + username);
        com.example.demo.model.domain.user.User savedUser =
                userRepository
                        .findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy email người dùng"));

        log.info(savedUser.toString());

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + savedUser.getRole()));
        return User.builder()
                .username(savedUser.getEmail())
                .password(savedUser.getPassword())
                .authorities(authorities)
                .build();
    }
}
