-- select database
USE CS144;

-- drop existing tables
DROP TABLE IF EXISTS Posts;

-- create table Posts
CREATE TABLE Posts(
  username	VARCHAR(40),
  postid	INTEGER,
  title		VARCHAR(100),
  body		TEXT,
  modified	TIMESTAMP DEFAULT '2000-01-01 00:00:00',
  created	TIMESTAMP DEFAULT '2000-01-01 00:00:00',
  PRIMARY KEY(username, postid)
);


DROP TABLE IF EXISTS Users;

-- This table will store every user and their last postid
-- so we can easily increment postid in table Posts
CREATE TABLE Users(
	username VARCHAR(40),
	postid INTEGER,
	PRIMARY KEY(username, postid)
);
