package com.example.springsecurity.config.security_context;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class CustomSecurityContextHolder {

//    @Bean
    public InitializingBean initializingBean() {
        return () -> SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_THREADLOCAL); // default
//        return () -> SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
//        return () -> SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_GLOBAL);
    }

}
