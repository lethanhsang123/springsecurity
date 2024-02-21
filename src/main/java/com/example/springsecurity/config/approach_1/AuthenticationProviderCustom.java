//package com.example.springsecurity.config.approach_1;
//
//import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class AuthenticationProviderCustom implements AuthenticationProvider {
//
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//
//        String username = authentication.getName();
//        String password = String.valueOf(authentication.getCredentials());
//
//        // Todo: authentication logic
//
//        if (!"admin".equals(username) || !"admin".equals(password)) throw new AuthenticationCredentialsNotFoundException("Login fail");
//
//        return new UsernamePasswordAuthenticationToken(username, password, List.of());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}
