package com.example.springsecurity.service.impl;

import com.example.springsecurity.entity.*;
import com.example.springsecurity.model.request.AuthenticationRequest;
import com.example.springsecurity.repository.*;
import com.example.springsecurity.service.AdminService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final StringKeyGenerator stringKeyGenerator;

    private final UserAuthorityRepository userAuthorityRepository;

    private final UserRoleRepository userRoleRepository;

    private final AuthorityRepository authorityRepository;

    private final RoleRepository roleRepository;

    public AdminServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                            AuthenticationManager authenticationManager, StringKeyGenerator stringKeyGenerator,
                            UserAuthorityRepository userAuthorityRepository, UserRoleRepository userRoleRepository,
                            AuthorityRepository authorityRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.stringKeyGenerator = stringKeyGenerator;
        this.userAuthorityRepository = userAuthorityRepository;
        this.userRoleRepository = userRoleRepository;
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
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
        user = userRepository.save(user);

        Integer uId = user.getId();
        // create authorities for user
        if (request.getAuthorities() != null) {
            List<Authority> authorities = authorityRepository.findAllByNames(request.getAuthorities());
            List<UserAuthority> userAuthorities = authorities
                    .stream()
                    .map(authority -> UserAuthority.builder().authorityId(authority.getId()).uId(uId).build())
                    .toList();
            userAuthorityRepository.saveAll(userAuthorities);
        }

        // create roles for user
        if (request.getRoles() != null) {
            List<Role> roles = roleRepository.findAllByNames(request.getRoles());
            List<UserRole> userRoles = roles
                    .stream()
                    .map(role -> UserRole.builder().roleId(role.getId()).uId(uId).build())
                    .toList();
            userRoleRepository.saveAll(userRoles);
        }
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
