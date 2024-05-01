ALTER TABLE tasks
    ADD COLUMN project_id BIGINT;

ALTER TABLE tasks
    ADD FOREIGN KEY (project_id) REFERENCES projects (id);