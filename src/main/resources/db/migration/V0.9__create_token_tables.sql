CREATE TABLE oauth_access_token (
    token_id varchar(250),
    token bytea NOT NULL,
    authentication_id varchar(250),
    user_name varchar(250) NOT NULL,
    client_id varchar(250) NOT NULL,
    authentication bytea NOT NULL,
    refresh_token varchar(250) NOT NULL,

    PRIMARY KEY (token_id, authentication_id)
);

CREATE TABLE oauth_refresh_token (
    token_id varchar(250) NOT NULL,
    token bytea NOT NULL,
    authentication bytea NOT NULL
);
