create table j_user
(
    id      serial primary key,
    name    text,
    role_id int not null references j_role (id)
);