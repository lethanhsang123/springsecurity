//package com.example.springsecurity.config.approach_1;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
//public class SecurityConfigurer {
//
//    public static final String[] URL_WHITE_LIST = {
//            "/security/api/v1/admin/registration",
//            "/security/api/v1/admin/authentication"
//    };
//
//    private final AuthenticationProvider authenticationProviderCustom;
//
//    public SecurityConfigurer(AuthenticationProvider authenticationProviderCustom) {
//        this.authenticationProviderCustom = authenticationProviderCustom;
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .httpBasic(Customizer.withDefaults())
//                .authorizeHttpRequests(req -> req
//                        .requestMatchers(URL_WHITE_LIST).permitAll()
//                        .anyRequest().authenticated()
//                )
//        ;
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, UserDetailsService userDetailsService)
//            throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.authenticationProvider(this.authenticationProviderCustom);
//        authenticationManagerBuilder.userDetailsService(userDetailsService);
//        return authenticationManagerBuilder.build();
//    }
//
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetailsService userDetailsService = new InMemoryUserDetailsManager();
//        return userDetailsService;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }
//
//}
