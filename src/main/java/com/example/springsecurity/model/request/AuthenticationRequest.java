package com.example.springsecurity.model.request;

import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class AuthenticationRequest {

    private String email;
    private String password;
    private String name;
    private List<String> roles;
    private List<String> authorities;
}
