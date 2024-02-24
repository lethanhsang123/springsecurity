package com.example.springsecurity.config.approach_2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfigurer {

    public static final String[] URL_WHITE_LIST = {
            "/security/api/v1/admin/registration",
            "/security/api/v1/admin/authentication"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(Customizer.withDefaults())
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("security/api/v1/admin/authentication").permitAll()
                        .requestMatchers("security/api/v1/admin/registration").hasRole("ADMIN")
                        .requestMatchers("security/api/v1/admin/test").hasAuthority("READ")
                        .requestMatchers("security/api/v1/admin/test2").hasAuthority("WRITE")
                        .anyRequest().authenticated()
                )
        ;
        return http.build();
    }
}
