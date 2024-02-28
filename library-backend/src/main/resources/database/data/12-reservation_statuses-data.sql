--liquibase formatted sql
--changeset adrian:1
insert into reservation_statuses(name) values ('aktywna');
insert into reservation_statuses(name) values ('zakończona');
insert into reservation_statuses(name) values ('anulowana');