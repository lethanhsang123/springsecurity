<<<<<<< Updated upstream
package com.example.resourceserver.configurers.security.authorization;

import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//@EnableWebSecurity
public class SecurityConfigurer {

    private final ServletPolicyEnforcerFilter servletPolicyEnforcerFilter;

    public SecurityConfigurer(ServletPolicyEnforcerFilter servletPolicyEnforcerFilter) {
        this.servletPolicyEnforcerFilter = servletPolicyEnforcerFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(servletPolicyEnforcerFilter, BearerTokenAuthenticationFilter.class)
                .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;
        return http.build();
    }



}
=======
//package com.example.resourceserver.configurers.security.authorization;
//
//import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfigurer {
//
//    private final ServletPolicyEnforcerFilter servletPolicyEnforcerFilter;
//
//    public SecurityConfigurer(ServletPolicyEnforcerFilter servletPolicyEnforcerFilter) {
//        this.servletPolicyEnforcerFilter = servletPolicyEnforcerFilter;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .addFilterAfter(servletPolicyEnforcerFilter, BearerTokenAuthenticationFilter.class)
//                .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//        ;
//        return http.build();
//    }
//
//
//
//}
>>>>>>> Stashed changes
