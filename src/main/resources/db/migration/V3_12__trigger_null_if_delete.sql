CREATE OR REPLACE FUNCTION clear_user_quarantine_on_eventreg_delete()
RETURNS trigger AS $$
BEGIN
  UPDATE users u
  SET quarantine_until = NULL
  WHERE u.id = OLD.user_id;
  RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_clear_user_quarantine_on_delete
AFTER DELETE ON event_reg2
FOR EACH ROW EXECUTE FUNCTION clear_user_quarantine_on_eventreg_delete();