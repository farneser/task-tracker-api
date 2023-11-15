CREATE TABLE columns_log
(
    id              SERIAL PRIMARY KEY,
    event_type      VARCHAR(10) NOT NULL,
    column_id       BIGINT,
    column_name     VARCHAR(255),
    user_id         BIGINT,
    event_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_columns_event() RETURNS TRIGGER AS
$$
BEGIN

    INSERT INTO columns_log (event_type, column_id, column_name, user_id, event_timestamp)
    VALUES (TG_OP, NEW.id, NEW.column_name, NEW.user_id, NOW());

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER columns_event_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON columns
    FOR EACH ROW
EXECUTE FUNCTION log_columns_event();
