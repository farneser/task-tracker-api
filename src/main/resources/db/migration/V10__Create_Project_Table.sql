CREATE TABLE projects
(
    id           SERIAL PRIMARY KEY,
    project_name VARCHAR(255)
);

ALTER TABLE tasks
    ADD COLUMN status_id BIGINT;

ALTER TABLE tasks
    ADD FOREIGN KEY (status_id) REFERENCES statuses (id)
