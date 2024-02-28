--liquibase formatted sql
--changeset adrian:1
insert into books(title, title_original, description, language_id) values ('Książka1', 'The book1', null, 2);
insert into books(title, title_original, description, language_id) values ('Książka2', 'The book2', null, 2);
insert into books(title, title_original, description, language_id) values ('Książka3', 'The book3', null, 2);