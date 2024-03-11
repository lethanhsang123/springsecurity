package com.example.authorizationserver.services;

import com.example.authorizationserver.entities.*;
import com.example.authorizationserver.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ClientService implements RegisteredClientRepository {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void save(RegisteredClient registeredClient) {
        Client c = new Client();

        c.setClientId(registeredClient.getClientId());
        c.setSecret(passwordEncoder.encode(registeredClient.getClientSecret()));
        c.setAuthenticationMethods(registeredClient.getClientAuthenticationMethods()
                .stream().map(a -> AuthenticationMethod.from(a, c)).collect(Collectors.toSet()));
        c.setGrantTypes(
                registeredClient.getAuthorizationGrantTypes().stream()
                        .map(g -> GrantType.from(g, c)).collect(Collectors.toSet())
        );
        c.setRedirectUrls(registeredClient.getRedirectUris().stream()
                .map(r -> RedirectUrl.from(r, c)).collect(Collectors.toSet()));

        c.setScopes(registeredClient.getScopes().stream()
                .map(s -> Scope.from(s, c)).collect(Collectors.toSet()));
        clientRepository.save(c);
    }

    @Override
    public RegisteredClient findById(String id) {
        Optional<Client> client = clientRepository.findById(Integer.parseInt(id));

        return client.map(Client::fromClient)
                .orElseThrow(() -> new RuntimeException(":("));

    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Optional<Client> client = clientRepository.findByClientId(clientId);

        return client.map(Client::fromClient)
                .orElseThrow(() -> new RuntimeException(":("));
    }
}
