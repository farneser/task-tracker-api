CREATE TABLE tasks
(
    id            SERIAL PRIMARY KEY,
    task_name     VARCHAR(255),
    description   VARCHAR(255),
    user_id       BIGINT,
    column_id     BIGINT,
    order_number  BIGINT,
    creation_date timestamp not null default now(),
    edit_date     timestamp not null default now(),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (column_id) REFERENCES columns (id)
);
