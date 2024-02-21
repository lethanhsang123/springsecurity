package com.example.springsecurity.service;

import com.example.springsecurity.model.request.AuthenticationRequest;

public interface AdminService {
    String authenticate(AuthenticationRequest request);

    void registration(AuthenticationRequest request);
}
