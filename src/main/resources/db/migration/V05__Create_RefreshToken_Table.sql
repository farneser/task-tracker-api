CREATE TABLE refresh_tokens
(
    id              SERIAL PRIMARY KEY,
    token           VARCHAR(255) NOT NULL UNIQUE,
    expiration_date TIMESTAMP    NOT NULL,
    user_id         BIGINT       NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);