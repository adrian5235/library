--liquibase formatted sql
--changeset adrian:1
create table editions (
    id int auto_increment primary key,
    isbn13 varchar(255) not null unique,
    name varchar(255) null,
    release_year varchar(255) not null,
    number_of_pages int not null,
    weight varchar(255) null,
    dimensions varchar(255) null,
    quantity int null default 0,
    book_id int not null,
    publisher_id int not null,
    translator_id int null,
    binding_id int not null,
    language_id int not null,
    foreign key (book_id) references books(id),
    foreign key (publisher_id) references publishers(id),
    foreign key (translator_id) references translators(id),
    foreign key (binding_id) references bindings(id),
    foreign key (language_id) references languages(id)
);