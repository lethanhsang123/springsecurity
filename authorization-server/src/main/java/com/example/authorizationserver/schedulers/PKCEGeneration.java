package com.example.authorizationserver.schedulers;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class PKCEGeneration {

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
    }

}
