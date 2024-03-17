--liquibase formatted sql
--changeset adrian:1
insert into copies(signature, purchase_date, price, edition_id)
values ('COPY/01', '2023-03-25', 50, 1);
insert into copies(signature, purchase_date, price, edition_id)
values ('COPY/02', '2023-03-25', 50, 3);
insert into copies(signature, purchase_date, price, edition_id)
values ('COPY/03', '2023-03-25', 50, 4);