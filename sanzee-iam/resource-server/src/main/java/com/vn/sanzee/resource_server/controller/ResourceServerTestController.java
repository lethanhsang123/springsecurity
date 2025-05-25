package com.vn.sanzee.resource_server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/resources")
public class ResourceServerTestController {

    private static final Logger log = LoggerFactory.getLogger(ResourceServerTestController.class);

    @GetMapping("/test")
    public Mono<Map<String, Object>> getArticles(@AuthenticationPrincipal Jwt jwt) {
        log.info("ResourceServerTestController - getArticles - JWT: {}", jwt);
        return Mono.just(jwt.getClaims());
    }

}
