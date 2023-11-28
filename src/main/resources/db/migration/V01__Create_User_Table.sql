CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    is_subscribed BOOLEAN      NOT NULL DEFAULT FALSE,
    register_date timestamp    not null default now()
);
