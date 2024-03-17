--liquibase formatted sql
--changeset adrian:1
insert into borrowing_statuses(name) values ('awaiting');
insert into borrowing_statuses(name) values ('active');
insert into borrowing_statuses(name) values ('finalized');
insert into borrowing_statuses(name) values ('canceled');