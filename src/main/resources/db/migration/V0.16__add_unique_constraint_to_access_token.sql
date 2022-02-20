ALTER TABLE oauth_access_token
    ADD UNIQUE (authentication_id);
