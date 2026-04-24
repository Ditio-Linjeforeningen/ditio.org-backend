CREATE TABLE IF NOT EXISTS event_reg2 (
    event_reg_id uuid PRIMARY KEY, 
    user_id varchar(255) NOT NULL,  
    event_id uuid NOT NULL,
    att_status varchar(50) NOT NULL
)