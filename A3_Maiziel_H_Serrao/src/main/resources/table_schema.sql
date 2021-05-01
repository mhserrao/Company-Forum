CREATE TABLE IF NOT EXISTS Messages(
id LONG PRIMARY KEY AUTO_INCREMENT,
thread_id VARCHAR(255),
author VARCHAR(255),
message VARCHAR(255),
message_date DATE,
message_time TIME
);

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS authorities;
create table users (
 username varchar(50) not null primary key,
 password varchar(120) not null,
 enabled boolean not null
);
create table authorities (
 username varchar(50) not null,
 authority varchar(50) not null,
 foreign key (username) references users (username)
);