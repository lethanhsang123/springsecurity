package com.example.springsecurity.service.impl;

import com.example.springsecurity.entity.User;
import com.example.springsecurity.model.request.AuthenticationRequest;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.service.AdminService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AdminServiceImpl(UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String authenticate(AuthenticationRequest request) {

        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return request.getEmail();

    }

    @Override
    @Transactional
    public void registration(AuthenticationRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setHash(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }
}
