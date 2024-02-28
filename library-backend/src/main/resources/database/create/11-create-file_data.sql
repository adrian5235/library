--liquibase formatted sql
--changeset adrian:1
create table file_data (
    id int auto_increment primary key,
    name varchar(255) not null,
    type varchar(255) not null,
    file_path varchar(255) not null,
    edition_id int null,
    foreign key (edition_id) references editions(id)
);