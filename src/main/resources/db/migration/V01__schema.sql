CREATE TABLE rabin_paya_user(
                            id VARCHAR(255) PRIMARY KEY,
                            username VARCHAR(255) UNIQUE NOT NULL ,
                            password VARCHAR(255) UNIQUE NOT NULL ,
                            phone VARCHAR(255) UNIQUE NOT NULL ,
                            creation_timestamp TIMESTAMP default now()
);

CREATE TABLE rabin_paya_role(
                            id VARCHAR(255) PRIMARY KEY,
                            title VARCHAR(255) UNIQUE NOT NULL ,
                            creation_timestamp TIMESTAMP default now()
);

CREATE TABLE user_roles(
                            user_id VARCHAR(255) REFERENCES rabin_paya_user(id),
                            role_id VARCHAR(255) REFERENCES rabin_paya_role(id)

);

CREATE TABLE auth_method(
                            id VARCHAR(255),
                            name VARCHAR(255) UNIQUE NOT NULL

);

CREATE TABLE grant_type(
                            id VARCHAR(255),
                            name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE scope(
                            id VARCHAR(255) ,
                            name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE redirect_uri(
                            id VARCHAR(255) ,
                            uri VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE client (
                            id VARCHAR(255) NOT NULL,
                            client_id VARCHAR(255) NOT NULL,
                            client_id_issued_at TIMESTAMP DEFAULT now() NOT NULL,
                            client_secret VARCHAR(255) DEFAULT NULL,
                            client_secret_expires_at TIMESTAMP DEFAULT NULL,
                            client_name VARCHAR(255) NOT NULL,
                            client_settings VARCHAR(2000),
                            token_settings VARCHAR(2000),
                            PRIMARY KEY (id)
);

CREATE TABLE authorized_scope(
                             id VARCHAR(255) ,
                             name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE oauth2_authorization (
                               id varchar(255) NOT NULL,
                               registered_client_id varchar(255) NOT NULL,
                               principal_name varchar(255) NOT NULL,
                               authorization_grant_type varchar(255) NOT NULL,
                               authorized_scopes varchar(1000) DEFAULT NULL,
                               attributes varchar(4000) DEFAULT NULL,
                               auth_state varchar(500) DEFAULT NULL,
                               authorization_code_value varchar(4000) DEFAULT NULL,
                               authorization_code_issued_at timestamp DEFAULT NULL,
                               authorization_code_expires_at timestamp DEFAULT NULL,
                               authorization_code_metadata varchar(2000) DEFAULT NULL,
                               access_token_value varchar(4000) DEFAULT NULL,
                               access_token_issued_at timestamp DEFAULT NULL,
                               access_token_expires_at timestamp DEFAULT NULL,
                               access_token_metadata varchar(2000) DEFAULT NULL,
                               access_token_type varchar(255) DEFAULT NULL,
                               access_token_scopes varchar(1000) DEFAULT NULL,
                               refresh_token_value varchar(4000) DEFAULT NULL,
                               refresh_token_issued_at timestamp DEFAULT NULL,
                               refresh_token_expires_at timestamp DEFAULT NULL,
                               refresh_token_metadata varchar(2000) DEFAULT NULL,
                               oidc_id_token_value varchar(4000) DEFAULT NULL,
                               oidc_id_token_issued_at timestamp DEFAULT NULL,
                               oidc_id_token_expires_at timestamp DEFAULT NULL,
                               oidc_id_token_metadata varchar(2000) DEFAULT NULL,
                               oidc_id_token_claims varchar(2000) DEFAULT NULL,
                               PRIMARY KEY (id)
);

CREATE TABLE authorization_consent (
                              id VARCHAR(255) PRIMARY KEY,
                              client_id varchar(255) NOT NULL REFERENCES client(id),
                              user_id varchar(255) NOT NULL REFERENCES rabin_paya_user(id),
                              authorities varchar(1000) NOT NULL
);