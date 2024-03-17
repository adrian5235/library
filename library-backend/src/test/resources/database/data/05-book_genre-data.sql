--liquibase formatted sql
--changeset adrian:1
insert into book_genre(book_id, genre_id) values (1, 1);
insert into book_genre(book_id, genre_id) values (1, 2);