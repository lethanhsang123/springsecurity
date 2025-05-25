package com.vn.sanzee.oauth2_client.security;

import com.vn.sanzee.oauth2_client.repository.Oauth2RegisteredClientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {


    private static final String LOGIN_PAGE_URI = "/login";
    private static final String [] WHITE_LIST = {
            "/assets/**",
            "/error",
            LOGIN_PAGE_URI,
            "/svg/*",
            "/img/*",
            "/favicon.ico",
    };

    /**
     * Defines reactive security rules (e.g., path authorization, OAuth 2.0 login, logout).
     * @param http ServerHttpSecurity
     * @return SecurityWebFilterChain
     */
    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(authorizeExchanges ->
                        authorizeExchanges
                                .pathMatchers(WHITE_LIST).permitAll()
                                .anyExchange().authenticated()
                )
                .formLogin(formLogin -> formLogin.loginPage(LOGIN_PAGE_URI))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                //.oauth2Login(Customizer.withDefaults()) // Use Case: “Login with GitHub/Google” scenarios where you need end‐user identity in your app.
                .oauth2Client(Customizer.withDefaults()) // Use Case: Server‐to‐server or UI‐to‐API token acquisition where you only need an access token, not a logged‐in user.
        ;
        return http.build();
    }


    @Bean
    ReactiveUserDetailsService users() {
        UserDetails user = User.withUsername("admin")
                .password("{noop}password")
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    /**
     * Create WebClient bean
     * WebClient: for making requests to the resource server.
     * ServerOAuth2AuthorizedClientExchangeFilterFunction: Integrates WebClient with OAuth 2.0, injecting access tokens into requests.
     * @param reactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager
     * @return WebClient
     */
    @Bean
    WebClient webClient(ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction serverOAuth2AuthorizedClientExchangeFilterFunction =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(reactiveOAuth2AuthorizedClientManager);
        serverOAuth2AuthorizedClientExchangeFilterFunction.setDefaultOAuth2AuthorizedClient(true);
        return WebClient.builder()
                .filter(serverOAuth2AuthorizedClientExchangeFilterFunction)
                .build();
    }

    /**
     * Create ReactiveOAuth2AuthorizedClientManager bean
     * The ReactiveOAuth2AuthorizedClientManager is responsible for the overall management of OAuth2AuthorizedClient(s),
     *  manages token lifecycle (fetch, refresh, persist) for authorized clients.
     * @param reactiveClientRegistrationRepository reactiveClientRegistrationRepository
     * @param reactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService.
     * @return ReactiveOAuth2AuthorizedClientManager
     */
    @Bean
    ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager(
            ReactiveClientRegistrationRepository reactiveClientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService
    ) {
        // ReactiveOAuth2AuthorizedClientProvider: implements a strategy for authorizing (or re-authorizing) an OAuth 2.0 Client.
        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .refreshToken()
                .clientCredentials()
                .provider(new JwtBearerReactiveOAuth2AuthorizedClientProvider())
                .build();

        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(reactiveClientRegistrationRepository, reactiveOAuth2AuthorizedClientService);
        reactiveOAuth2AuthorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return reactiveOAuth2AuthorizedClientManager;
    }

    /**
     * Create ServerOAuth2AuthorizedClientRepository bean
     * ServerOAuth2AuthorizedClientRepository is responsible for persisting OAuth2AuthorizedClient(s) between web requests.
     * @param reactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService
     * @return ServerOAuth2AuthorizedClientRepository
     */
    @Bean
    ServerOAuth2AuthorizedClientRepository serverOAuth2AuthorizedClientRepository(ReactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService) {
        return new AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository(reactiveOAuth2AuthorizedClientService);
    }

    /**
     * Create ReactiveOAuth2AuthorizedClientService bean
     * ReactiveOAuth2AuthorizedClientService is to manage OAuth2AuthorizedClient(s) at the application-level.
     * @param databaseClient R2DBC database client
     * @param clientRegistrationRepository ReactiveClientRegistrationRepository
     * @return ReactiveOAuth2AuthorizedClientService
     */
    @Bean
    ReactiveOAuth2AuthorizedClientService reactiveOAuth2AuthorizedClientService(DatabaseClient databaseClient,
                                                                                ReactiveClientRegistrationRepository clientRegistrationRepository) {
        return new R2dbcReactiveOAuth2AuthorizedClientService(databaseClient, clientRegistrationRepository);
    }

    /**
     * Create ReactiveClientRegistrationRepository bean
     * The ReactiveClientRegistrationRepository serves as a repository for OAuth 2.0 / OpenID Connect 1.0 ClientRegistration(s).
     * @param oauth2RegisteredClientRepository R2DBC oauth2RegisteredClientRepository
     * @return ReactiveClientRegistrationRepository
     */
    @Bean
    ReactiveClientRegistrationRepository clientRegistrationRepository(Oauth2RegisteredClientRepository oauth2RegisteredClientRepository,
                                                                      DatabaseClient databaseClient) {
        return new R2dbcClientRegistrationRepository(oauth2RegisteredClientRepository, databaseClient);
    }

    /**
     * Create ClientRegistration bean
     * Client registration information is ultimately stored and owned by the associated Authorization Server.
     * This repository provides the ability to retrieve a sub-set of the primary client registration information,
     * which is stored with the Authorization Server.
     * @return ClientRegistration
     */
    private ClientRegistration clientRegistration1() {
        return ClientRegistration.withRegistrationId("messaging-client-authorization-code")
                .clientId("client")
                .clientSecret("secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}") // Used by the authorization server to return responses containing authorization credentials to the client via the resource owner user-agent.
                .scope("message.read", "message.write")
                .clientName("messaging-client-authorization-code")
                .authorizationUri("http://127.0.0.1:9000/oauth2/authorize") // Used by the client to obtain authorization from the resource owner via user-agent redirection.
                .tokenUri("http://127.0.0.1:9000/oauth2/token") // Used by the client to exchange an authorization grant for an access token, typically with client authentication.
                .build();
    }
    private ClientRegistration clientRegistration2() {
        return ClientRegistration.withRegistrationId("messaging-client-oidc")
                .clientId("client")
                .clientSecret("secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}") // Used by the authorization server to return responses containing authorization credentials to the client via the resource owner user-agent.
                .scope(OidcScopes.OPENID, OidcScopes.PROFILE)
                .clientName("messaging-client-oidc")
                .issuerUri("http://127.0.0.1:9000")
                .authorizationUri("http://localhost:9000/oauth2/authorize") // Used by the client to obtain authorization from the resource owner via user-agent redirection.
                .tokenUri("http://127.0.0.1:9000/oauth2/token") // Used by the client to exchange an authorization grant for an access token, typically with client authentication.
                .jwkSetUri("http://127.0.0.1:9000/oauth2/jwks")
                .userInfoUri("http://127.0.0.1:9000/connect/userinfo")   // Used by the client to retrieve user information (resource owner) using access token.
                .userNameAttributeName("sub") // The attribute name in the user info response that contains the unique identifier for the user.
                .build();
    }
    private ClientRegistration clientRegistration3() {
        return ClientRegistration.withRegistrationId("messaging-client-model")
                .clientId("client")
                .clientSecret("secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}") // Used by the authorization server to return responses containing authorization credentials to the client via the resource owner user-agent.
                .scope("message.read")
                .clientName("messaging-client-model")
                .tokenUri("http://127.0.0.1:9000/oauth2/token") // Used by the client to exchange an authorization grant for an access token, typically with client authentication.
                .build();
    }
    private ClientRegistration clientRegistration4() {
        return ClientRegistration.withRegistrationId("messaging-client-pkce")
                .clientId("client")
                .clientSecret("secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}") // Used by the authorization server to return responses containing authorization credentials to the client via the resource owner user-agent.
                .scope("message.read")
                .clientName("messaging-client-pkce")
                .tokenUri("http://127.0.0.1:9000/oauth2/token") // Used by the client to exchange an authorization grant for an access token, typically with client authentication.
                .authorizationUri("http://127.0.0.1:9000/oauth2/authorize") // Used by the client to obtain authorization from the resource owner via user-agent redirection.
                .build();
    }


}
