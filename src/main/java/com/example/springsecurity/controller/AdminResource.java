package com.example.springsecurity.controller;

import com.example.springsecurity.model.request.AuthenticationRequest;
import com.example.springsecurity.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminResource {

    private final AdminService adminService;

    public AdminResource(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/authentication")
    public ResponseEntity<?> authentication(@RequestBody AuthenticationRequest request) {
        String result = adminService.authenticate(request);
        if (result == null) {
            return ResponseEntity.badRequest().body("Login fail");
        }
        return ResponseEntity.ok("Login success");
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody AuthenticationRequest request) {
        adminService.registration(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        adminService.logout();
        return ResponseEntity.ok("Logout success");
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("TESTTTTTTT");
    }

}
