package com.example.springsecurity.config.approach_2.authentication_provider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;

@Configuration
public class AuthenticationProviders {

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProviderCustom();
    }

}
