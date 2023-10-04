create table participates
(
    id      serial primary key,
    item_id int not null references items (id),
    user_id int not null references j_user (id),
    unique (item_id, user_id)
);
