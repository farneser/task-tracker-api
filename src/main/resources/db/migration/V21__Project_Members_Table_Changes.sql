CREATE TABLE project_members_history
(
    history_id       SERIAL PRIMARY KEY,
    action_type      VARCHAR(50)             NOT NULL,
    action_timestamp TIMESTAMP DEFAULT NOW() NOT NULL,
    member_id        BIGINT                  NOT NULL,
    username         VARCHAR(255),
    project_id       BIGINT                  NOT NULL,
    project_name     VARCHAR(255),
    role             VARCHAR(255)
);

CREATE OR REPLACE FUNCTION log_project_member_changes() RETURNS TRIGGER AS
$$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO project_members_history (action_type, member_id, username, project_id, project_name, role,
                                             action_timestamp)
        SELECT 'INSERT', NEW.member_id, users.username, NEW.project_id, projects.project_name, NEW.role, NOW()
        FROM users
                 JOIN projects ON NEW.project_id = projects.id
        WHERE users.id = NEW.member_id;
    ELSIF TG_OP = 'UPDATE' THEN
        IF NEW.member_id <> OLD.member_id OR NEW.project_id <> OLD.project_id OR NEW.role <> OLD.role THEN
            INSERT INTO project_members_history (action_type, member_id, username, project_id, project_name, role,
                                                 action_timestamp)
            SELECT 'UPDATE', NEW.member_id, users.username, NEW.project_id, projects.project_name, NEW.role, NOW()
            FROM users
                     JOIN projects ON NEW.project_id = projects.id
            WHERE users.id = NEW.member_id;
        END IF;
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO project_members_history (action_type, member_id, username, project_id, project_name, role,
                                             action_timestamp)
        SELECT 'DELETE', OLD.member_id, users.username, OLD.project_id, projects.project_name, OLD.role, NOW()
        FROM users
                 JOIN projects ON OLD.project_id = projects.id
        WHERE users.id = OLD.member_id;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER project_members_insert_trigger
    AFTER INSERT
    ON project_members
    FOR EACH ROW
EXECUTE FUNCTION log_project_member_changes();

CREATE TRIGGER project_members_update_trigger
    AFTER UPDATE
    ON project_members
    FOR EACH ROW
    WHEN (NEW.member_id <> OLD.member_id OR NEW.project_id <> OLD.project_id OR NEW.role <> OLD.role)
EXECUTE FUNCTION log_project_member_changes();

CREATE TRIGGER project_members_delete_trigger
    AFTER DELETE
    ON project_members
    FOR EACH ROW
EXECUTE FUNCTION log_project_member_changes();
