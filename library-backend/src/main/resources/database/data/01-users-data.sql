--liquibase formatted sql
--changeset adrian:1
insert into _users(email, name, password, role)
values ('reader@localhost', 'Reader Localhost', '$2a$10$4m2nPywBnG.AtXNMGIErzeOTiPh1yhdrqXP4acMajcwv5zltC0Mhe', 'READER');
insert into _users(email, name, password, role)
values ('librarian@localhost', 'Librarian Localhost', '$2a$10$YaSHfuqGkq4Jj5zoEyL61e1M7AskipByCl1fEHYOc.5gT4/i7OVSm', 'LIBRARIAN');
insert into _users(email, name, password, role)
values ('admin@localhost', 'Admin Localhost', '$2a$10$3/PZ3IJPItLDvft3OrJ9HOl9VO38UWF/w2PTRhs3m70vZ6rgSOopu', 'ADMIN');
insert into _users(email, name, password, role)
values ('john@localhost', 'John Localhost', '$2a$12$4Yp49IlVcG/5KMd3TWRuNedDdqoDHi8O0QZxKwxAAUxWSzRBI4Kj2', 'READER');
insert into _users(email, name, password, role)
values ('michael@localhost', 'Michael Localhost', '$2a$12$qhbftk44nX8/obds4EPhK.zo5fiQBq9jRgd8ApmwKyq00D7QV7Ur.', 'READER');