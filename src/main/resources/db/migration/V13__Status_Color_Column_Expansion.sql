-- Add a new column with the desired length (7 characters).
ALTER TABLE statuses
    ADD COLUMN new_status_color VARCHAR(7);

-- Populate the new column with data from the existing column.
UPDATE statuses
SET new_status_color = status_color
where TRUE;

-- Drop the old column.
ALTER TABLE statuses
    DROP COLUMN status_color;

-- Rename the new column to match the old column name if necessary.
ALTER TABLE statuses
    RENAME COLUMN new_status_color TO status_color;