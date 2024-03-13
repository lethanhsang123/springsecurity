package com.example.authorizationserver.services;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
@AllArgsConstructor
public class PKCEGeneration {

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void pkceGenerate() throws NoSuchAlgorithmException {

        // CODE VERIFIER
        SecureRandom secureRandom = new SecureRandom();
        byte [] code = new byte[32];
        secureRandom.nextBytes(code);
        String codeVerifier = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(code);

        // CODE CHALLENGE
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte [] digested = messageDigest.digest(codeVerifier.getBytes());
        String codeChallenge = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(digested);
        System.out.println("VERIFIER: " + codeVerifier);
        System.out.println("CHALLENGE: : " + codeChallenge);
        System.out.println("encoder(secret): " + passwordEncoder.encode("secret"));
    }

}
