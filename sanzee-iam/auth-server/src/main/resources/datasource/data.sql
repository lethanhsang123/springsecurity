INSERT INTO auth_server.oauth2_registered_client (id, client_id, client_id_issued_at, client_secret,
                                                  client_secret_expires_at, client_name, client_authentication_methods,
                                                  authorization_grant_types, redirect_uris, post_logout_redirect_uris,
                                                  scopes, client_settings, token_settings)
VALUES ('1', 'client', '2025-05-25 13:04:36', '{noop}secret', null, '1',
        'client_secret_post,private_key_jwt,self_signed_tls_client_auth,client_secret_jwt,tls_client_auth,client_secret_basic',
        'refresh_token,client_credentials,authorization_code',
        'http://127.0.0.1:8070/login/oauth2/code/messaging-client-authorization-code,http://127.0.0.1:8070/login/oauth2/code/messaging-client-oidc,http://127.0.0.1:8070/login/oauth2/code/messaging-client-model',
        'http://127.0.0.1:8070/logout/success', 'address,phone,openid,profile,message.read,email,message.write',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.x509-certificate-bound-access-tokens":false,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",1800.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}');
