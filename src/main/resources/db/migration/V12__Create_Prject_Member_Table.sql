CREATE TABLE project_members
(
    id         SERIAL PRIMARY KEY,
    member_id  BIGINT,
    project_id BIGINT,
    role       VARCHAR(255),
    FOREIGN KEY (member_id) REFERENCES users (id),
    FOREIGN KEY (project_id) REFERENCES projects (id)
);
