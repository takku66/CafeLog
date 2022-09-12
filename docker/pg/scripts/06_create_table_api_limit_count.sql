create table api_limit_count(
api_id integer references api_limit_manage(api_id),
called_at timestamp,
user_id integer,
primary key (api_id, called_at, user_id)
);