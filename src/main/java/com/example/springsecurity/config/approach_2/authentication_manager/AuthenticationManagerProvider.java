package com.example.springsecurity.config.approach_2.authentication_manager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class AuthenticationManagerProvider {

    private final AuthenticationProvider authenticationProvider;

    private final UserDetailsService userDetailsService;

    public AuthenticationManagerProvider(AuthenticationProvider authenticationProvider,
                                         UserDetailsService userDetailsService) {
        this.authenticationProvider = authenticationProvider;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(this.authenticationProvider);
        authenticationManagerBuilder.userDetailsService(this.userDetailsService);
        return authenticationManagerBuilder.build();
    }

}
