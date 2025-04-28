DROP TABLE IF EXISTS DM;

CREATE TABLE DM (
    message_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    sender_id VARCHAR(20) NOT NULL,
    receiver_id VARCHAR(20) NOT NULL,
    message_text VARCHAR(1000) NOT NULL,
    send_at TIMESTAMP NOT NULL,
    read_at TIMESTAMP,
    parent_message_id BIGINT,
    PRIMARY KEY (message_id, room_id)
);