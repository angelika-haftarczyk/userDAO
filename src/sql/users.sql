CREATE DATABASE workshop2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE users (
                       id INT AUTO_INCREMENT NOT NULL ,
                       email VARCHAR(255) NOT NULL ,
                       username VARCHAR(255) UNIQUE NOT NULL ,
                       password VARCHAR(60) NOT NULL ,
                       PRIMARY KEY (id)
);

INSERT INTO users(username, email, password) VALUES (?, ?, ?);

SELECT * FROM users WHERE id = ?;

UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?;

DELETE FROM users WHERE id = ?;

SELECT * FROM users;


