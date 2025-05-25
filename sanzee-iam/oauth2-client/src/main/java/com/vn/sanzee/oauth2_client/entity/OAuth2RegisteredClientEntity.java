package com.vn.sanzee.oauth2_client.entity;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Table(name = "oauth2_registered_client")
public record OAuth2RegisteredClientEntity(
        @Id
        @Column(value = "registration_id")
        String registrationId,

        @Column(value = "client_id")
        String clientId,

        @Column(value = "client_secret")
        String clientSecret,

        @Column(value = "client_authentication_method")
        String clientAuthenticationMethod,

        @Column(value = "authorization_grant_type")
        String authorizationGrantType,

        @Column(value = "client_name")
        String clientName,

        @Column(value = "redirect_uri")
        String redirectUri,

        @Column(value = "scopes")
        String scopes,

        @Column(value = "authorization_uri")
        String authorizationUri,

        @Column(value = "token_uri")
        String tokenUri,

        @Column(value = "jwk_set_uri")
        String jwkSetUri,

        @Column(value = "issuer_uri")
        String issuerUri,

        @Column(value = "user_info_uri")
        String userInfoUri,

        @Column(value = "user_info_authentication_method")
        String userInfoAuthenticationMethod,

        @Column(value = "user_name_attribute_name")
        String userNameAttributeName,

        @Column(value = "configuration_metadata")
        String configurationMetadata,

        @Column(value = "created_at")
        LocalDateTime createdAt) {

    public ClientRegistration toClientRegistration() {
        return ClientRegistration.withRegistrationId(registrationId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientAuthenticationMethod(new ClientAuthenticationMethod(clientAuthenticationMethod))
                .authorizationGrantType(new AuthorizationGrantType(authorizationGrantType))
                .clientName(clientName)
                .redirectUri(redirectUri)
                .scope(scopes != null ? List.of(scopes.split(",")) : Collections.emptyList())
                .authorizationUri(authorizationUri)
                .tokenUri(tokenUri)
                .jwkSetUri(jwkSetUri)
                .issuerUri(issuerUri)
                .userInfoUri(userInfoUri)
                .userInfoAuthenticationMethod(new AuthenticationMethod(userInfoAuthenticationMethod))
                .userNameAttributeName(userNameAttributeName)
                .providerConfigurationMetadata(parseMetadata(configurationMetadata))
                .build();
    }

    private static Map<String, Object> parseMetadata(String metadata) {
        if (metadata == null || metadata.isBlank()) {
            return Collections.emptyMap();
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(metadata, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to parse configuration metadata JSON", e);
        }
    }
}
