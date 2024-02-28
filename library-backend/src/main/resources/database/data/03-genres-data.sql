--liquibase formatted sql
--changeset adrian:1
insert into genres(name) values ('Fantasy');
insert into genres(name) values ('Horror');