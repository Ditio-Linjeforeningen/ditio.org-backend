ALTER TABLE item
  ADD COLUMN origin TEXT;              -- 1) legg til nullable

UPDATE item
  SET origin = 'unknown';              -- 2) backfill eksisterende rader

ALTER TABLE item
  ALTER COLUMN origin SET NOT NULL;    -- 3) stram inn constraint
