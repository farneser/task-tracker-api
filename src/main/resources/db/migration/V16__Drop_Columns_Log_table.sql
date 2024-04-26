DROP TRIGGER IF EXISTS columns_event_trigger ON statuses;

DROP FUNCTION IF EXISTS log_columns_event();

DROP TABLE IF EXISTS columns_log;

ALTER SEQUENCE columns_id_seq RENAME TO statuses_id_seq;