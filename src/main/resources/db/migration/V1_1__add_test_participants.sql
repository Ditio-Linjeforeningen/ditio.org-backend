CREATE TABLE IF NOT EXISTS test_attendance_list (
    att_list_id UUID PRIMARY KEY DEFAULT uuidv7(),
    event_id INTEGER NOT NULL DEFAULT 0,
    userIdString TEXT NOT NULL,
    att_status boolean
);

INSERT INTO test_attendance_list (event_id, userIdString, att_status) VALUES
(123, 'OlaNor123', false),
(123, 'KariNor456', true),
(123, 'IvanIV789', false),
(123, 'Sotabror99', false),
(123, 'JohnSmith9000', false),
(123, 'PierreRobert75', false),
(123, 'HansGutenberg1000', false),
(123, 'Ahmed1997', false);
