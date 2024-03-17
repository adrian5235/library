--liquibase formatted sql
--changeset adrian:1
insert into bindings(name) values ('Paperback');
insert into bindings(name) values ('Hardcover');