--liquibase formatted sql
--changeset adrian:1
create table copies (
    id int auto_increment primary key,
    signature varchar(255) not null unique,
    purchase_date date not null,
    price double not null,
    available bit null default 1,
    edition_id int not null,
    foreign key (edition_id) references editions(id)
);