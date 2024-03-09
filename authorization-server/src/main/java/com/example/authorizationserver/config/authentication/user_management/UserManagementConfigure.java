package com.example.authorizationserver.config.authentication.user_management;

import com.example.authorizationserver.repositories.AuthorityRepository;
import com.example.authorizationserver.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@AllArgsConstructor
public class UserManagementConfigure {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    @Bean
    public UserDetailsService userDetailsService() {
        return new com.example.authorizationserver.config.authentication.user_management.UserDetailsService(userRepository, authorityRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
