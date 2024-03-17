--liquibase formatted sql
--changeset adrian:1
insert into books(title, description, language_id) values ('The book1', null, 1);
insert into books(title, description, language_id) values ('The book2', null, 1);
insert into books(title, description, language_id) values ('The book3', null, 1);