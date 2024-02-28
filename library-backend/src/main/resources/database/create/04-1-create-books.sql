--liquibase formatted sql
--changeset adrian:1
create table books (
    id int auto_increment primary key,
    title varchar(255) not null,
    title_original varchar(255) null,
    description varchar(1000) null,
    language_id int not null,
    foreign key (language_id) references languages(id)
);