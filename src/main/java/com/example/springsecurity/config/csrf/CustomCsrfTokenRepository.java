package com.example.springsecurity.config.csrf;

import com.example.springsecurity.entity.Token;
import com.example.springsecurity.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class CustomCsrfTokenRepository implements CsrfTokenRepository {
    public static final String DEFAULT_CSRF_HEADER_NAME = "X-CSRF-TOKEN";
    public static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";
    // Every client have unique identifier in header
    public static final String DEFAULT_CLIENT_IDENTIFIER_HEADER_NAME = "X-IDENTIFIER";
    public static final String DEFAULT_CSRF_CLIENT_IDENTIFIER_PREFIX = "CSRF_";

    private final TokenRepository tokenRepository;

    public CustomCsrfTokenRepository(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        String token = UUID.randomUUID().toString();
        return new DefaultCsrfToken(DEFAULT_CSRF_HEADER_NAME, DEFAULT_CSRF_PARAMETER_NAME, token);
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        // get client identifier from header
        String identifier = request.getHeader(DEFAULT_CLIENT_IDENTIFIER_HEADER_NAME);
        if ( Objects.isNull(identifier) || identifier.isEmpty() || identifier.isBlank()) return;
        // check token exist in DB(Redis is recommend)
        Optional<Token> csrfToken = tokenRepository.findByIdentifier(DEFAULT_CSRF_CLIENT_IDENTIFIER_PREFIX + identifier);
        if (csrfToken.isPresent()) {
            // exist client identifier in DB => update token
            csrfToken.get().setToken(token.getToken());
        } else {
            // client identifier does not exist in DB => create token
            csrfToken = Optional.of(Token.builder().identifier(DEFAULT_CSRF_CLIENT_IDENTIFIER_PREFIX + identifier).token(token.getToken()).build());
            tokenRepository.save(csrfToken.get());
        }
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        String identifier = request.getHeader(DEFAULT_CLIENT_IDENTIFIER_HEADER_NAME);
        if (Objects.isNull(identifier) || identifier.isEmpty() || identifier.isBlank()) return null;
        Optional<Token> existingToken = tokenRepository.findByIdentifier(DEFAULT_CSRF_CLIENT_IDENTIFIER_PREFIX + identifier);
        if (existingToken.isPresent()) {
            Token token = existingToken.get();
            return new DefaultCsrfToken(DEFAULT_CSRF_HEADER_NAME, DEFAULT_CSRF_PARAMETER_NAME, token.getToken());
        }
        return null;
    }
}
