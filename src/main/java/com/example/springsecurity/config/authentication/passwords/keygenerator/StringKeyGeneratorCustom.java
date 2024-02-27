package com.example.springsecurity.config.authentication.passwords.keygenerator;

import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.stereotype.Component;

@Component
public class StringKeyGeneratorCustom implements StringKeyGenerator {

    private final StringKeyGenerator keyGenerators = KeyGenerators.string();

    @Override
    public String generateKey() {
        // 8-byte key, encodes as a hexadecimal string
        return this.keyGenerators.generateKey();
    }
}
