create table favorites(
user_id integer references users(user_id),
cafe_id integer references cafe(cafe_id),
primary key (user_id, cafe_id)
);