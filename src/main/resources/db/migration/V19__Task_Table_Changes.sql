CREATE TABLE tasks_history
(
    history_id       SERIAL PRIMARY KEY,
    action_type      VARCHAR(50)             NOT NULL,
    action_timestamp TIMESTAMP DEFAULT NOW() NOT NULL,
    task_id          BIGINT                  NOT NULL,
    task_name        VARCHAR(255),
    description      TEXT,
    user_id          BIGINT,
    status_id        BIGINT,
    status_name      VARCHAR(255),
    order_number     BIGINT,
    project_id       BIGINT,
    project_name     VARCHAR(255)
);

CREATE OR REPLACE FUNCTION log_task_changes() RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO tasks_history (action_type, task_id, task_name, description, user_id, status_id, status_name, order_number, project_id, project_name)
        SELECT 'INSERT', NEW.id, NEW.task_name, NEW.description, NEW.user_id, NEW.status_id, statuses.status_name, NEW.order_number, NEW.project_id, projects.project_name
        FROM statuses
                 JOIN projects ON NEW.project_id = projects.id
        WHERE statuses.id = NEW.status_id;
    ELSIF TG_OP = 'UPDATE' THEN
        IF NEW.task_name <> OLD.task_name OR NEW.description <> OLD.description OR NEW.user_id <> OLD.user_id OR NEW.status_id <> OLD.status_id OR NEW.project_id <> OLD.project_id THEN
            INSERT INTO tasks_history (action_type, task_id, task_name, description, user_id, status_id, status_name, order_number, project_id, project_name)
            SELECT 'UPDATE', NEW.id, NEW.task_name, NEW.description, NEW.user_id, NEW.status_id, statuses.status_name, NEW.order_number, NEW.project_id, projects.project_name
            FROM statuses
                     JOIN projects ON NEW.project_id = projects.id
            WHERE statuses.id = NEW.status_id;
        END IF;
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO tasks_history (action_type, task_id, task_name, description, user_id, status_id, status_name, order_number, project_id, project_name)
        SELECT 'DELETE', OLD.id, OLD.task_name, OLD.description, OLD.user_id, OLD.status_id, statuses.status_name, OLD.order_number, OLD.project_id, projects.project_name
        FROM statuses
                 JOIN projects ON OLD.project_id = projects.id
        WHERE statuses.id = OLD.status_id;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tasks_insert_trigger
    AFTER INSERT ON tasks
    FOR EACH ROW
EXECUTE FUNCTION log_task_changes();

CREATE TRIGGER tasks_update_trigger
    AFTER UPDATE ON tasks
    FOR EACH ROW
    WHEN (NEW.task_name <> OLD.task_name OR NEW.description <> OLD.description OR NEW.user_id <> OLD.user_id OR NEW.status_id <> OLD.status_id OR NEW.project_id <> OLD.project_id)
EXECUTE FUNCTION log_task_changes();

CREATE TRIGGER tasks_delete_trigger
    AFTER DELETE ON tasks
    FOR EACH ROW
EXECUTE FUNCTION log_task_changes();
