CREATE TABLE columns
(
    id            SERIAL PRIMARY KEY,
    column_name   VARCHAR(255),
    user_id       BIGINT,
    is_completed  BOOLEAN,
    order_number  BIGINT,
    creation_date timestamp not null default now(),
    edit_date     timestamp not null default now(),
    FOREIGN KEY (user_id) REFERENCES users (id)
);
