package com.example.springsecurity.config.filters;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class InitialAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    private final String signingKey = "TEST";

    public InitialAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String code = response.getHeader("code");
        String username = response.getHeader("username");
        String password = response.getHeader("password");

        if (null == code || code.isEmpty() || code.isBlank()) {
            // UsernameAndPasswordAuthenticationCustom implements
            Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
            // Custom authentication provider for UsernameAndPasswordAuthenticationCustom
            authenticationManager.authenticate(authentication);
        } else {
            // OTP Authentication implements
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, code);
            // Custom authentication provider for OTP Authentication
            authenticationManager.authenticate(authentication);

            SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));

            String jwt = Jwts.builder()
                    .setClaims(Map.of("username", username))
                    .signWith(key)
                    .compact();
            response.setHeader("Authorization", jwt);
        }


    }
}
