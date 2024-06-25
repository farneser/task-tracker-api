CREATE TABLE statuses_history
(
    history_id       SERIAL PRIMARY KEY,
    action_type      VARCHAR(50)             NOT NULL,
    action_timestamp TIMESTAMP DEFAULT NOW() NOT NULL,
    status_id        BIGINT                  NOT NULL,
    status_name      VARCHAR(255),
    is_completed     BOOLEAN,
    order_number     BIGINT,
    creation_date    TIMESTAMP               NOT NULL,
    edit_date        TIMESTAMP               NOT NULL,
    project_id       BIGINT,
    status_color     VARCHAR(7)
);

CREATE OR REPLACE FUNCTION log_status_changes() RETURNS TRIGGER AS
$$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO statuses_history (action_type, status_id, status_name, is_completed, order_number, creation_date,
                                      edit_date, project_id, status_color, action_timestamp)
        VALUES ('INSERT', NEW.id, NEW.status_name, NEW.is_completed, NEW.order_number, NEW.creation_date, NEW.edit_date,
                NEW.project_id, NEW.status_color, NOW());
    ELSIF TG_OP = 'UPDATE' THEN
        IF NEW.status_name <> OLD.status_name OR NEW.is_completed <> OLD.is_completed OR
           NEW.order_number <> OLD.order_number OR NEW.project_id <> OLD.project_id OR
           NEW.status_color <> OLD.status_color THEN
            INSERT INTO statuses_history (action_type, status_id, status_name, is_completed, order_number,
                                          creation_date, edit_date, project_id, status_color, action_timestamp)
            VALUES ('UPDATE', NEW.id, NEW.status_name, NEW.is_completed, NEW.order_number, NEW.creation_date,
                    NEW.edit_date, NEW.project_id, NEW.status_color, NOW());
        END IF;
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO statuses_history (action_type, status_id, status_name, is_completed, order_number, creation_date,
                                      edit_date, project_id, status_color, action_timestamp)
        VALUES ('DELETE', OLD.id, OLD.status_name, OLD.is_completed, OLD.order_number, OLD.creation_date, OLD.edit_date,
                OLD.project_id, OLD.status_color, NOW());
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER statuses_insert_trigger
    AFTER INSERT
    ON statuses
    FOR EACH ROW
EXECUTE FUNCTION log_status_changes();

CREATE TRIGGER statuses_update_trigger
    AFTER UPDATE
    ON statuses
    FOR EACH ROW
    WHEN (NEW.status_name <> OLD.status_name OR NEW.is_completed <> OLD.is_completed OR
          NEW.order_number <> OLD.order_number OR NEW.project_id <> OLD.project_id OR
          NEW.status_color <> OLD.status_color)
EXECUTE FUNCTION log_status_changes();

CREATE TRIGGER statuses_delete_trigger
    AFTER DELETE
    ON statuses
    FOR EACH ROW
EXECUTE FUNCTION log_status_changes();
