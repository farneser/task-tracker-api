CREATE TABLE columns
(
    id          SERIAL PRIMARY KEY,
    column_name VARCHAR(255),
    user_id     BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
