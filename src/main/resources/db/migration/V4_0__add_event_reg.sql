-- EventReg tabell
CREATE TABLE event_reg (
    event_id UUID NOT NULL,
    id VARCHAR NOT NULL,
    status VARCHAR NOT NULL,
    waitlist INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (event_id, id),
    FOREIGN KEY (event_id) REFERENCES event(event_id),
    FOREIGN KEY (id) REFERENCES users(id)
);

-- RegEventSheet tabell
CREATE TABLE reg_event_sheet (
    reg_id BIGSERIAL PRIMARY KEY,
    event_id UUID NOT NULL,
    id VARCHAR NOT NULL,
    study_programme VARCHAR,
    study_year INT,
    food_preference VARCHAR,
    FOREIGN KEY (event_id,id) REFERENCES event_reg(event_id,id)
);