package com.example.authorizationserver.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Entity
@Table(name = "clients")
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "client_id")
    private String clientId;

    private String secret;

//    private String authentication;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<GrantType> grantTypes;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Scope> scopes;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<RedirectUrl> redirectUrls;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<AuthenticationMethod> authenticationMethods;

    @OneToOne(mappedBy = "client")
    private ClientTokenSettings clientTokenSettings;

    public static RegisteredClient fromClient(Client client) {
        return RegisteredClient.withId(String.valueOf(client.getId()))
                .clientId(client.getClientId())
                .clientSecret(client.getSecret())
                .clientAuthenticationMethods(clientAuthenticationMethods(client.getAuthenticationMethods()))
                .authorizationGrantTypes(authorizationGrantTypes(client.getGrantTypes()))
                .scopes(scopes(client.getScopes()))
                .redirectUris(redirectUris(client.getRedirectUrls()))
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(client.clientTokenSettings.getAccessTokenTTL()))
                        .accessTokenFormat(new OAuth2TokenFormat(client.clientTokenSettings.getType()))
                        .build())
                .build();
    }

    private static Consumer<Set<AuthorizationGrantType>> authorizationGrantTypes(Set<GrantType> grantTypes) {
        return m -> {
            grantTypes.forEach(grantType -> {
                m.add(new AuthorizationGrantType(grantType.getGrantType()));
            });
        };
    }

    private static Consumer<Set<ClientAuthenticationMethod>> clientAuthenticationMethods(Set<AuthenticationMethod> authenticationMethods) {
        return m -> {
            authenticationMethods.forEach(a -> {
                m.add(new ClientAuthenticationMethod(a.getAuthenticationMethod()));
            });
        };
    }

    private static Consumer<Set<String>> scopes(Set<Scope> scopes) {
        return m -> {
            scopes.forEach(a -> {
                m.add(a.getScope());
            });
        };
    }

    private static Consumer<Set<String>> redirectUris(Set<RedirectUrl> redirectUrls) {
        return m -> {
            redirectUrls.forEach(a -> {
                m.add(a.getUrl());
            });
        };
    }
}
