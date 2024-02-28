--liquibase formatted sql
--changeset adrian:1
insert into book_author(book_id, author_id) values (1, 1);