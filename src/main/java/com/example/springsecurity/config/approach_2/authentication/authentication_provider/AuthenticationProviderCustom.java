package com.example.springsecurity.config.approach_2.authentication.authentication_provider;

import com.example.springsecurity.config.approach_2.authentication.manage_users.SecurityUser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProviderCustom implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    public AuthenticationProviderCustom(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserByUsername(email);
        String rawPassword = securityUser.getSalt() + password;
        if (!passwordEncoder.matches(rawPassword, securityUser.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        return UsernamePasswordAuthenticationToken.authenticated(email, securityUser.getPassword(), securityUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
