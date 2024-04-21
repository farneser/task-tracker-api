ALTER TABLE users
    ADD COLUMN is_locked BOOLEAN not null default false;

ALTER TABLE users
    ADD COLUMN role VARCHAR(255) not null default 'USER';

ALTER TABLE users
    ADD COLUMN username VARCHAR(255) not null unique default '';

CREATE UNIQUE INDEX username_index ON users (username);
