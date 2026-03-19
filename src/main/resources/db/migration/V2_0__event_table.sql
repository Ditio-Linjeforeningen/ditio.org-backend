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