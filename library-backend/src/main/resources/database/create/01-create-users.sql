--liquibase formatted sql
--changeset adrian:1
create table _users (
    id int auto_increment primary key,
    email varchar(255) not null unique,
    name varchar(255) not null,
    password varchar(255) not null,
    action_points int null default 5,
    role enum('ADMIN', 'LIBRARIAN', 'READER', 'USER') null
);