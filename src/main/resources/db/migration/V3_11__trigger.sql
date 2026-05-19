CREATE OR REPLACE FUNCTION sync_user_quarantine()
RETURNS trigger AS $$
BEGIN
  UPDATE users u
  SET quarantine_until = NEW.quarantine_end
  WHERE u.id = NEW.user_id;
  RETURN NEW;
END; $$ LANGUAGE plpgsql;

CREATE TRIGGER trg_sync_user_quarantine
AFTER INSERT OR UPDATE OF quarantine_end ON event_reg2
FOR EACH ROW EXECUTE FUNCTION sync_user_quarantine();