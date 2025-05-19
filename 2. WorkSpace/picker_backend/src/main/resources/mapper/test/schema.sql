SET GLOBAL event_scheduler = ON;

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

CREATE TABLE IF NOT EXISTS post_likes(
    post_id BIGINT NOT NULL,
    user_id VARCHAR(20) NOT NULL,
    CONSTRAINT uq_likes UNIQUE (post_id, user_id),
    FOREIGN (post_id) REFERENCES post(post_id),
)

CREATE PROCEDURE IF NOT EXISTS markRepliesDeleted()
BEGIN
	UPDATE reply r
	INNER JOIN post p ON r.post_id = p.post_id
	SET r.is_deleted = 1
	WHERE p.is_deleted = 1
	AND r.is_deleted = 0;
END

CREATE EVENT IF NOT EXISTS dailyMarkRepliesDeleted
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_DATE + INTERVAL 1 DAY
DO CALL markRepliesDeleted();

CREATE PROCEDURE IF NOT EXISTS markLikesDeleted()
BEGIN
	DELETE pl FROM post_likes pl
	INNER JOIN post p on p.post_id = pl.post_id
	WHERE p.is_deleted = 1;
END;

CREATE EVENT IF NOT EXISTS dailyMarkLikesDeleted
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_DATE + INTERVAL 1 DAY
DO CALL markLikesDeleted();