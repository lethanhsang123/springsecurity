package com.example.resourceserver.configurers.security;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
public class KeycloakSecurityUtil {

    Keycloak keycloak;

    @Value("${keycloak.configurations.server-uri}")
    private String serverUri;

    @Value("${keycloak.configurations.realm}")
    private String realm;

    @Value("${keycloak.configurations.client-id}")
    private String clientId;

    @Value("${keycloak.configurations.grant-type}")
    private String grantType;

    @Value("${keycloak.configurations.name}")
    private String username;

    @Value("${keycloak.configurations.password}")
    private String password;

    public Keycloak getKeycloakInstance() {
        if(keycloak == null) {
            keycloak = KeycloakBuilder
                    .builder().serverUrl(serverUri).realm(realm)
                    .clientId(clientId).grantType(grantType)
                    .username(username).password(password).build();
        }
        return keycloak;
    }

}
