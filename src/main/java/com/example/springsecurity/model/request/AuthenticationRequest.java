package com.example.springsecurity.model.request;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String email;
    private String password;
    private String name;
}
