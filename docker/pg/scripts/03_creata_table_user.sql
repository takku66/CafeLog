create table users (
user_id serial ,
name varchar(500),
email varchar(500),
PRIMARY KEY (user_id)
);

CREATE INDEX ON users (email);