ALTER TABLE columns
    RENAME TO statuses;

ALTER TABLE statuses
    RENAME COLUMN column_name TO status_name;

ALTER TABLE statuses
    ADD COLUMN status_color VARCHAR(6);

ALTER TABLE statuses
    ADD COLUMN project_id BIGINT;

ALTER TABLE statuses
    ADD FOREIGN KEY (project_id) REFERENCES projects (id);

ALTER TABLE tasks
    RENAME COLUMN column_id TO status_id;

ALTER TABLE tasks
    ADD FOREIGN KEY (status_id) REFERENCES statuses (id)
