--liquibase formatted sql
--changeset adrian:1
create table reservations (
    id int auto_increment primary key,
    created_on date not null,
    wait_deadline date null,
    notes varchar(255) null,
    status_id int not null,
    book_id int not null,
    edition_id int null,
    user_id int not null,
    foreign key (status_id) references reservation_statuses(id),
    foreign key (book_id) references books(id),
    foreign key (edition_id) references editions(id),
    foreign key (user_id) references _users(id)
);