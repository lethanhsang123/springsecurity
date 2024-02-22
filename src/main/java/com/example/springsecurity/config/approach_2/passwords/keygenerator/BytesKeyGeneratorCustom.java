package com.example.springsecurity.config.approach_2.passwords.keygenerator;

import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;

public class BytesKeyGeneratorCustom implements BytesKeyGenerator {

    private final Integer KEY_LENGTH = 8;

    // default key_length = 8
    private final BytesKeyGenerator bytesKeyGenerator = KeyGenerators.secureRandom(KEY_LENGTH);

    @Override
    public int getKeyLength() {
        return this.bytesKeyGenerator.getKeyLength();
    }

    @Override
    public byte[] generateKey() {
        return this.bytesKeyGenerator.generateKey();
    }
}
