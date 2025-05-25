CREATE TABLE IF NOT EXISTS oauth2_authorized_client (
    client_registration_id VARCHAR(100) NOT NULL,
    principal_name VARCHAR(200) NOT NULL,
    access_token_type VARCHAR(100) NOT NULL,
    access_token_value BYTEA NOT NULL,
    access_token_issued_at TIMESTAMP NOT NULL,
    access_token_expires_at TIMESTAMP NOT NULL,
    access_token_scopes VARCHAR(1000),
    refresh_token_value BYTEA,
    refresh_token_issued_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (client_registration_id, principal_name)
    );

CREATE TABLE IF NOT EXISTS oauth2_registered_client (
    registration_id VARCHAR(100) NOT NULL,
    client_id VARCHAR(100) NOT NULL,
    client_secret VARCHAR(200),
    client_authentication_method VARCHAR(100) NOT NULL,
    authorization_grant_type VARCHAR(100) NOT NULL,
    client_name VARCHAR(200),
    redirect_uri VARCHAR(1000) NOT NULL,
    scopes VARCHAR(1000) NOT NULL,
    authorization_uri VARCHAR(1000),
    token_uri VARCHAR(1000) NOT NULL,
    jwk_set_uri VARCHAR(1000),
    issuer_uri VARCHAR(1000),
    user_info_uri VARCHAR(1000),
    user_info_authentication_method VARCHAR(100),
    user_name_attribute_name VARCHAR(100),
    configuration_metadata TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (registration_id)
    );