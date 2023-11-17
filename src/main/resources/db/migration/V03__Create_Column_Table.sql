CREATE TABLE columns
(
    id           SERIAL PRIMARY KEY,
    column_name  VARCHAR(255),
    user_id      BIGINT,
    is_completed BOOLEAN,
    order_number BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
