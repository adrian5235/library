--liquibase formatted sql
--changeset adrian:1
create table translators (
    id int auto_increment primary key,
    name varchar(255) not null unique
);