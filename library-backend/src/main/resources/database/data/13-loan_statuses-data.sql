--liquibase formatted sql
--changeset adrian:1
insert into loan_statuses(name) values ('oczekujące');
insert into loan_statuses(name) values ('aktywne');
insert into loan_statuses(name) values ('zakończone');
insert into loan_statuses(name) values ('anulowane');