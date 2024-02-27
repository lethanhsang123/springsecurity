package com.example.springsecurity.config.authentication.authentication_manager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@Configuration
public class AuthenticationManagerCustom {

    private final AuthenticationProvider authenticationProvider;

    private final UserDetailsService userDetailsService;

    public AuthenticationManagerCustom(AuthenticationProvider authenticationProvider,
                                       UserDetailsService userDetailsService) {
        this.authenticationProvider = authenticationProvider;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity)
            throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(this.authenticationProvider);
        authenticationManagerBuilder.userDetailsService(this.userDetailsService);
        return authenticationManagerBuilder.build();
    }
}
