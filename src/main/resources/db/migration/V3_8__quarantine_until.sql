ALTER TABLE users
ADD COLUMN if not exists quarantine_until TIMESTAMP;