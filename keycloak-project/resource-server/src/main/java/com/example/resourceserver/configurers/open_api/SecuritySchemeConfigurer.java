package com.example.resourceserver.configurers.open_api;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
//@SecurityScheme(
//        name = "Keycloak",
//        openIdConnectUrl = "http://localhost:8080/realms/dive-dev/.well-known/openid-configuration",
//        scheme = "bearer",
//        type = SecuritySchemeType.OPENIDCONNECT,
//        in = SecuritySchemeIn.HEADER
//)
public class SecuritySchemeConfigurer {
}
