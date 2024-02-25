package com.example.springsecurity.model.request;

import lombok.Data;

import java.util.Collection;

@Data
public class AuthenticationRequest {

    private String email;
    private String password;
    private String name;
    private Collection<String> roles;
    private Collection<String> authorities;
}
