package com.example.springsecurity.controller;

import com.example.springsecurity.model.request.AuthenticationRequest;
import com.example.springsecurity.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.concurrent.DelegatingSecurityContextCallable;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("TESTTTTTTT");
    }

    @PostMapping("/test2")
    @PreAuthorize("hasAuthority('WRITE')")
    public ResponseEntity<?> test2() {
        return ResponseEntity.ok("TESTTTTTTT2");
    }
    @GetMapping("/anonymous")
    public ResponseEntity<?> anonymous() {
        return ResponseEntity.ok("OKOK");
    }

    @GetMapping("/ciao")
    public String ciao() throws Exception {
        Callable<String> task = () -> {
            SecurityContext context = SecurityContextHolder.getContext();
            return context.getAuthentication().getName();
        };
        ExecutorService e = Executors.newCachedThreadPool();
        try {
            /**
             *  return "Ciao, " + e.submit(task).get() + "!";
             *  -> Get nothing more than a NullPointerException
             *  Inside the newly created thread to run the callable task, the authentication does not exist anymore,
             *  and the security context is empty. To solve this problem we decorate the task with DelegatingSecurityContextCallable,
             *  which copy the current context to the new thread
             */
            var contextTask = new DelegatingSecurityContextCallable<>(task);
            return "Ciao, " + e.submit(contextTask).get() + "!";
        } finally {
            e.shutdown();
        }
    }

    @GetMapping("/v1/ciao")
    public String ciaoV2() throws Exception {
        Callable<String> task = () -> {
            SecurityContext context = SecurityContextHolder.getContext();
            return context.getAuthentication().getName();
        };
        ExecutorService e = Executors.newCachedThreadPool();
        e = new DelegatingSecurityContextExecutorService(e);
        try {
            return "Ciao, " + e.submit(task).get() + "!";
        } finally {
            e.shutdown();
        }
    }

}
