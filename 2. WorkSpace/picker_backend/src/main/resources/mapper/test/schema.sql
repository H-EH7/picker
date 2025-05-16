CREATE TABLE IF NOT EXISTS post (
    post_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(20),
    post_text VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted BIT NOT NULL DEFAULT 0,
    temp_id VARCHAR(200),
    view_count INT
);

CREATE TABLE IF NOT EXISTS reply(
	reply_id BIGINT PRIMARY KEY AUTO_INCREMENT,
	parent_reply_id BIGINT,
	post_id BIGINT Not Null,
	user_id VARCHAR(20) Not Null,
	reply_text VARCHAR(1000) Not Null,
	created_at TIMESTAMP Not Null default current_timestamp,
	updated_at TimeSTAMP default current_timestamp ON update current_timestamp,
	is_deleted BIT Not Null default 0,
	temp_id VARCHAR(200)
	FOREIGN Key (post_id) REFERENCES post(post_id),
	FOREIGN Key (parent_reply_id) REFERENCES reply(reply_id)
)
