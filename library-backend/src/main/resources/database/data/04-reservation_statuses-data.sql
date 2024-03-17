--liquibase formatted sql
--changeset adrian:1
insert into reservation_statuses(name) values ('active');
insert into reservation_statuses(name) values ('finalized');
insert into reservation_statuses(name) values ('canceled');