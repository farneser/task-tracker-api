CREATE OR REPLACE FUNCTION update_column_order()
    RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        NEW.order_number = COALESCE((SELECT MAX(order_number) + 1 FROM columns), 1);

        UPDATE columns
        SET order_number = order_number + 1
        WHERE order_number >= NEW.order_number AND id != NEW.id;

    ELSIF TG_OP = 'UPDATE' AND NEW.order_number > OLD.order_number THEN
        UPDATE columns
        SET order_number = order_number + 1
        WHERE order_number >= NEW.order_number AND id != NEW.id;

    ELSIF TG_OP = 'UPDATE' AND NEW.order_number < OLD.order_number THEN
        UPDATE columns
        SET order_number = order_number - 1
        WHERE order_number <= OLD.order_number AND id != NEW.id;

    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_column_order_trigger
    BEFORE INSERT OR UPDATE ON columns
    FOR EACH ROW
EXECUTE FUNCTION update_column_order();
