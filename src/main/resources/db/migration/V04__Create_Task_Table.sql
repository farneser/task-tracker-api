CREATE TABLE tasks
(
    id           SERIAL PRIMARY KEY,
    task_name    VARCHAR(255),
    description  VARCHAR(255),
    user_id      BIGINT,
    column_id    BIGINT,
    order_number BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (column_id) REFERENCES columns (id)
);
