package com.example.authorizationserver.config.client_management;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.UUID;

@Configuration
public class ClientManagementConfigurer {

    @Bean
    public RegisteredClientRepository repository() {

        // Todo: Load registered clients from Database

        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("client")
                .clientSecret("secret")
                .scope("read")
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .build();

        return new InMemoryRegisteredClientRepository(client);
    }

}
