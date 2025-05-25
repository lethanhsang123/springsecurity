INSERT INTO public.oauth2_registered_client (registration_id, client_id, client_secret, client_authentication_method,
                                             authorization_grant_type, client_name, redirect_uri, scopes,
                                             authorization_uri, token_uri, jwk_set_uri, issuer_uri, user_info_uri,
                                             user_info_authentication_method, user_name_attribute_name,
                                             configuration_metadata, created_at)
VALUES ('messaging-client-authorization-code', 'client', 'secret', 'client_secret_basic', 'authorization_code',
        'messaging-client-authorization-code', '{baseUrl}/login/oauth2/code/{registrationId}',
        'message.read,message.write', 'http://127.0.0.1:9000/oauth2/authorize', 'http://127.0.0.1:9000/oauth2/token',
        null, null, null, 'header', null, '{}', '2025-05-25 13:19:35.591526');
INSERT INTO public.oauth2_registered_client (registration_id, client_id, client_secret, client_authentication_method,
                                             authorization_grant_type, client_name, redirect_uri, scopes,
                                             authorization_uri, token_uri, jwk_set_uri, issuer_uri, user_info_uri,
                                             user_info_authentication_method, user_name_attribute_name,
                                             configuration_metadata, created_at)
VALUES ('messaging-client-oidc', 'client', 'secret', 'client_secret_basic', 'authorization_code',
        'messaging-client-oidc', '{baseUrl}/login/oauth2/code/{registrationId}', 'openid,profile',
        'http://localhost:9000/oauth2/authorize', 'http://127.0.0.1:9000/oauth2/token',
        'http://127.0.0.1:9000/oauth2/jwks', 'http://127.0.0.1:9000', 'http://127.0.0.1:9000/connect/userinfo',
        'header', 'sub', '{}', '2025-05-25 13:19:35.603059');
INSERT INTO public.oauth2_registered_client (registration_id, client_id, client_secret, client_authentication_method,
                                             authorization_grant_type, client_name, redirect_uri, scopes,
                                             authorization_uri, token_uri, jwk_set_uri, issuer_uri, user_info_uri,
                                             user_info_authentication_method, user_name_attribute_name,
                                             configuration_metadata, created_at)
VALUES ('messaging-client-model', 'client', 'secret', 'client_secret_post', 'client_credentials',
        'messaging-client-model', '{baseUrl}/login/oauth2/code/{registrationId}', 'message.read', null,
        'http://127.0.0.1:9000/oauth2/token', null, null, null, 'header', null, '{}', '2025-05-25 13:19:35.607949');
