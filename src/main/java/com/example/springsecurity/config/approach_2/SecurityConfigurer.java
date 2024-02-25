package com.example.springsecurity.config.approach_2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfigurer {

    public static final String[] URL_WHITE_LIST = {
            "security/api/v1/admin/registration",
            "security/api/v1/admin/authentication",
            "security/api/v1/admin/anonymous"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String sQEL = "hasAuthority('READ') || hasRole('MANAGER') ";
        String sQEL2 = "hasAuthority('WRITE') || hasRole('MANAGER') ";
        http
                .httpBasic(Customizer.withDefaults())
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( (req) -> req
                        .requestMatchers(URL_WHITE_LIST).permitAll()
                        .requestMatchers("/security/api/v1/admin/registration").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET).access(new WebExpressionAuthorizationManager(sQEL))
                        .requestMatchers(HttpMethod.POST).access(new WebExpressionAuthorizationManager(sQEL2))
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .invalidateHttpSession(Boolean.TRUE)
                        .deleteCookies("JSESSIONID")
                        .logoutUrl("/security/api/v1/admin/logout")
                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                        .maximumSessions(1)
//                        .maxSessionsPreventsLogin(Boolean.TRUE)
//                )
        ;
        return http.build();
    }
}
