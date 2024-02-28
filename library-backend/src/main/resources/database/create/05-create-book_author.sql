--liquibase formatted sql
--changeset adrian:1
create table book_author (
    id int auto_increment primary key,
    book_id int not null,
    author_id int not null,
    foreign key (book_id) references books(id),
    foreign key (author_id) references authors(id)
);