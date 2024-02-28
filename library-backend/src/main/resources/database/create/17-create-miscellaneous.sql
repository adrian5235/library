--liquibase formatted sql
--changeset adrian:1
create table miscellaneous (
    id int auto_increment primary key,
    about varchar(2000) null,
    rules varchar(2000) null,
    contact varchar(500) null
);