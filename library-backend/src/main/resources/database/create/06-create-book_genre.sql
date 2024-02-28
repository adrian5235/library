--liquibase formatted sql
--changeset adrian:1
create table book_genre (
    id int auto_increment primary key,
    book_id int not null,
    genre_id int not null,
    foreign key (book_id) references books(id),
    foreign key (genre_id) references genres(id)
);