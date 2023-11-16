CREATE TABLE columns
(
    id           SERIAL PRIMARY KEY,
    column_name  VARCHAR(255),
    user_id      BIGINT,
    is_completed BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
