package com.example.springsecurity.config.approach_2.passwords;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderCustom implements PasswordEncoder {



    @Override
    public String encode(CharSequence rawPassword) {
        return null;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return false;
    }
}
