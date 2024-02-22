package com.example.springsecurity.service.impl;

import com.example.springsecurity.entity.User;
import com.example.springsecurity.model.request.AuthenticationRequest;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.service.AdminService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final StringKeyGenerator stringKeyGenerator;

    public AdminServiceImpl(UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            AuthenticationManager authenticationManager,
                            StringKeyGenerator stringKeyGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.stringKeyGenerator = stringKeyGenerator;
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
        userRepository.findByEmail(request.getEmail()).ifPresent(user1 -> {throw new RuntimeException("User exist in System");});
        String salt = stringKeyGenerator.generateKey();
        User user = User.builder()
                .salt(salt)
                .email(request.getEmail())
                .name(request.getName())
                .hash(passwordEncoder.encode(salt + request.getPassword()))
                .build();
        userRepository.save(user);
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
