-- Event table
CREATE TABLE IF NOT EXISTS event (
    event_id UUID PRIMARY KEY DEFAULT uuidv7(),
    title TEXT NOT NULL,
    description TEXT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    location TEXT,
    max_attendees INTEGER,
    is_published BOOLEAN DEFAULT false
);

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    navn VARCHAR(255),
    email VARCHAR(255),
    role VARCHAR(40) DEFAULT 'USER',
    quarantine_until TIMESTAMP
);

-- Event registration table (renamed from event_reg2 to event_reg)
CREATE TABLE IF NOT EXISTS event_reg (
    event_reg_id UUID PRIMARY KEY, 
    user_id VARCHAR(255) NOT NULL,  
    event_id UUID NOT NULL,
    att_status VARCHAR(50) NOT NULL,
    deadline TIMESTAMP,
    quarantine_end TIMESTAMP
);

-- Trigger function: Sync user quarantine from event_reg
CREATE OR REPLACE FUNCTION sync_user_quarantine()
RETURNS trigger AS $$
BEGIN
  UPDATE users u
  SET quarantine_until = NEW.quarantine_end
  WHERE u.id = NEW.user_id;
  RETURN NEW;
END; $$ LANGUAGE plpgsql;

CREATE TRIGGER trg_sync_user_quarantine
AFTER INSERT OR UPDATE OF quarantine_end ON event_reg
FOR EACH ROW EXECUTE FUNCTION sync_user_quarantine();

-- Trigger function: Clear user quarantine on event_reg delete
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
AFTER DELETE ON event_reg
FOR EACH ROW EXECUTE FUNCTION clear_user_quarantine_on_eventreg_delete();