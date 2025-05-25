package com.vn.sanzee.oauth2_client.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vn.sanzee.oauth2_client.entity.OAuth2RegisteredClientEntity;
import com.vn.sanzee.oauth2_client.repository.Oauth2RegisteredClientRepository;
import io.r2dbc.spi.R2dbcException;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;

public class R2dbcClientRegistrationRepository implements ReactiveClientRegistrationRepository, Iterable<ClientRegistration> {

    private static final String COLUMN_NAMES = "registration_id, client_id, client_secret, client_authentication_method, " +
            "authorization_grant_type, client_name, redirect_uri, scopes, authorization_uri, token_uri, " +
            "jwk_set_uri, issuer_uri, user_info_uri, user_info_authentication_method, user_name_attribute_name, configuration_metadata";
    private static final String TABLE_NAME = "oauth2_registered_client";
    private static final String INSERT_OAUTH2_REGISTERED_CLIENT_SQL = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAMES + ") " +
            "VALUES (:registrationId, :clientId, :clientSecret, :clientAuthenticationMethod, " +
            ":authorizationGrantType, :clientName, :redirectUri, :scopes, :authorizationUri, :tokenUri, " +
            ":jwkSetUri, :issuerUri, :userInfoEndpointUri, :userInfoEndpointAuthenticationMethod, " +
            ":userNameAttributeName, :configurationMetadata)";
    private final Oauth2RegisteredClientRepository oauth2RegisteredClientRepository;
    private final DatabaseClient databaseClient;

    public R2dbcClientRegistrationRepository(Oauth2RegisteredClientRepository oauth2RegisteredClientRepository, DatabaseClient databaseClient, ClientRegistration... clientRegistrations) {
        this.oauth2RegisteredClientRepository = oauth2RegisteredClientRepository;
        this.databaseClient = databaseClient;
        Arrays.stream(clientRegistrations).forEach(clientRegistration -> {
            this.insert(clientRegistration).block();
        });
    }

    @Override
    public Mono<ClientRegistration> findByRegistrationId(String registrationId) {
        return oauth2RegisteredClientRepository.findByRegistrationId(registrationId)
                .map(OAuth2RegisteredClientEntity::toClientRegistration)
                .switchIfEmpty(Mono.error(new ClientRegistrationNotFoundException("Client registration not found: " + registrationId)))
                .onErrorMap(e -> e instanceof R2dbcException,
                        e -> new ClientRegistrationRepositoryException("Database error", e));
    }

    @Override
    public Iterator<ClientRegistration> iterator() {
        // Fetch all registrations and convert to iterator
        List<ClientRegistration> registrations = oauth2RegisteredClientRepository.findAll()
                .map(OAuth2RegisteredClientEntity::toClientRegistration)
                .collectList()
                .block(); // Blocking call for Iterable
        return registrations != null ? registrations.iterator() : Collections.emptyIterator();
    }

    // Helper method to save a new client registration
    public Mono<ClientRegistration> save(ClientRegistration registration) {
        OAuth2RegisteredClientEntity entity = toEntity(registration);
        return oauth2RegisteredClientRepository.save(entity)
                .map(OAuth2RegisteredClientEntity::toClientRegistration);
    }

    public Mono<Void> insert(ClientRegistration clientRegistration) {
        return bindClientRegistration(databaseClient.sql(INSERT_OAUTH2_REGISTERED_CLIENT_SQL), clientRegistration)
                .then();
    }

    private DatabaseClient.GenericExecuteSpec bindClientRegistration(DatabaseClient.GenericExecuteSpec spec, ClientRegistration clientRegistration) {
        OAuth2RegisteredClientEntity entity = toEntity(clientRegistration);
        spec = spec
                .bind("registrationId", entity.registrationId())
                .bind("clientId", entity.clientId())
                .bind("clientSecret", entity.clientSecret())
                .bind("clientAuthenticationMethod", entity.clientAuthenticationMethod())
                .bind("authorizationGrantType", entity.authorizationGrantType())
                .bind("clientName", entity.clientName())
                .bind("redirectUri", entity.redirectUri())
                .bind("scopes", entity.scopes())
//                .bind("authorizationUri", provider.getAuthorizationUri())
//                .bind("tokenUri", provider.getTokenUri())
//                .bind("jwkSetUri", provider.getJwkSetUri())
//                .bind("issuerUri", provider.getIssuerUri())
//                .bind("userInfoEndpointUri", userInfo.getUri())
//                .bind("userInfoEndpointAuthenticationMethod", userInfo.getAuthenticationMethod().getValue())
//                .bind("userNameAttributeName", userInfo.getUserNameAttributeName())
//                .bind("configurationMetadata", serializeMetadata(provider.getConfigurationMetadata()))
        ;
        return bindAllNullable(spec,
                new BindParam<>("authorizationUri", entity.authorizationUri(), String.class),
                new BindParam<>("tokenUri", entity.tokenUri(), String.class),
                new BindParam<>("jwkSetUri", entity.jwkSetUri(), String.class),
                new BindParam<>("issuerUri", entity.issuerUri(), String.class),
                new BindParam<>("userInfoEndpointUri", entity.userInfoUri(), String.class),
                new BindParam<>("userInfoEndpointAuthenticationMethod", entity.userInfoAuthenticationMethod(), String.class),
                new BindParam<>("userNameAttributeName", entity.userNameAttributeName(), String.class),
                new BindParam<>("configurationMetadata", entity.configurationMetadata(), String.class)
                );
    }

    @SafeVarargs
    private DatabaseClient.GenericExecuteSpec bindAllNullable(DatabaseClient.GenericExecuteSpec spec, BindParam<?>... params) {
        for (BindParam<?> param : params) {
            spec = param.value() != null
                    ? spec.bind(param.name(), param.value())
                    : spec.bindNull(param.name(), param.type());
        }
        return spec;
    }

    private record BindParam<T>(String name, T value, Class<T> type) {}

    // Helper method to convert ClientRegistration to entity
    private OAuth2RegisteredClientEntity toEntity(ClientRegistration registration) {
        return new OAuth2RegisteredClientEntity(
                registration.getRegistrationId(),
                registration.getClientId(),
                registration.getClientSecret(),
                registration.getClientAuthenticationMethod().getValue(),
                registration.getAuthorizationGrantType().getValue(),
                registration.getClientName(),
                registration.getRedirectUri(),
                String.join(",", registration.getScopes()),
                registration.getProviderDetails().getAuthorizationUri(),
                registration.getProviderDetails().getTokenUri(),
                registration.getProviderDetails().getJwkSetUri(),
                registration.getProviderDetails().getIssuerUri(),
                registration.getProviderDetails().getUserInfoEndpoint().getUri(),
                registration.getProviderDetails().getUserInfoEndpoint().getAuthenticationMethod().getValue(),
                registration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(),
                serializeMetadata(registration.getProviderDetails().getConfigurationMetadata()),
                LocalDateTime.now()
        );
    }

    private String serializeMetadata(Map<String, Object> metadata) {
        if (metadata == null || metadata.isEmpty()) {
            return "{}";
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(metadata);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize configuration metadata to JSON", e);
        }
    }
}
class ClientRegistrationNotFoundException extends RuntimeException {
    public ClientRegistrationNotFoundException(String message) {
        super(message);
    }
}

class ClientRegistrationRepositoryException extends RuntimeException {
    public ClientRegistrationRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}