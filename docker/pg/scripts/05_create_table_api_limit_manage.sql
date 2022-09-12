create table api_limit_manage(
api_id integer ,
api_name varchar(200),
limit_count integer,
limit_interval_type char(2),
primary key (api_id)
);