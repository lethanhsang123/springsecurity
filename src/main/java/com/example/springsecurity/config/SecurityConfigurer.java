package com.example.springsecurity.config;

import com.example.springsecurity.config.csrf.CustomCsrfTokenRepository;
import com.example.springsecurity.config.filters.CsrfTokenLogger;
import com.example.springsecurity.repository.TokenRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfigurer {

    public static final String[] AUTHENTICATION_URI_WHITE_LIST = {
            "security/api/v1/admin/registration",
            "security/api/v1/admin/authentication",
            "security/api/v1/admin/anonymous"
    };
    public static final String[] CSRF_URI_WHITE_LIST = {
            "security/api/v1/admin/registration",
            "security/api/v1/admin/authentication"
    };

    // Inject CsrfTokenRepository Been
    private final CsrfTokenRepository customCsrfTokenRepository;
    private final CsrfTokenRequestHandler csrfTokenRequestAttributeHandler;

    public SecurityConfigurer(CsrfTokenRepository customCsrfTokenRepository) {
        this.customCsrfTokenRepository = customCsrfTokenRepository;
        this.csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // default password: 123456
        String sQEL = "hasAuthority('READ') || hasRole('MANAGER') ";
        String sQEL2 = "hasAuthority('WRITE') || hasRole('MANAGER') ";
        String sQEL3 = "hasAuthority('UPDATE') || hasRole('MANAGER') ";
        String sQEL4 = "hasAuthority('DELETE') || hasRole('MANAGER') ";

        // RegexRequestMatcher for CSRF Filter
        String pattern = ".*[0-9].*";
        String httpMethod = HttpMethod.POST.name();
        RegexRequestMatcher r = new RegexRequestMatcher(pattern, httpMethod);


        http
                .httpBasic(Customizer.withDefaults())
                .cors(AbstractHttpConfigurer::disable)
                .csrf(csrf -> {
                    csrf.ignoringRequestMatchers(CSRF_URI_WHITE_LIST);
                    csrf.csrfTokenRepository(customCsrfTokenRepository);
                    csrf.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler);
                    /*csrf.ignoringRequestMatchers(r);*/
                })
                .authorizeHttpRequests( (req) -> req
                        .requestMatchers(AUTHENTICATION_URI_WHITE_LIST).permitAll()
                        .requestMatchers("/security/api/v1/admin/registration").hasRole("ADMIN")
                        /*.requestMatchers(HttpMethod.GET).access(new WebExpressionAuthorizationManager(sQEL))
                        .requestMatchers(HttpMethod.POST).access(new WebExpressionAuthorizationManager(sQEL2))
                        .requestMatchers(HttpMethod.PUT).access(new WebExpressionAuthorizationManager(sQEL3))
                        .requestMatchers(HttpMethod.PATCH).access(new WebExpressionAuthorizationManager(sQEL3))
                        .requestMatchers(HttpMethod.DELETE).access(new WebExpressionAuthorizationManager(sQEL4))*/
                        .anyRequest().authenticated()
                )
                .addFilterAfter(new CsrfTokenLogger(), CsrfFilter.class)
                .logout(logout -> logout
                        .invalidateHttpSession(Boolean.TRUE)
                        .deleteCookies("JSESSIONID")
                        .logoutUrl("/security/api/v1/admin/logout")
                )
                /*.sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(Boolean.TRUE)
                )*/
        ;
        return http.build();
    }
}
