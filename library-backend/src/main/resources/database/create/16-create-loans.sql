--liquibase formatted sql
--changeset adrian:1
create table loans (
    id int auto_increment primary key,
    created_on date not null,
    pick_up_deadline date null,
    picked_up_on date null,
    return_deadline date null,
    returned_on date null,
    charge double null default 0,
    charge_paid double null default 0,
    charged_on date null,
    has_charge_been_paid bit null default 0,
    notes varchar(255) null,
    status_id int not null,
    copy_id int not null,
    user_id int not null,
    reservation_id int null,
    foreign key (status_id) references loan_statuses(id),
    foreign key (copy_id) references copies(id),
    foreign key (user_id) references _users(id),
    foreign key (reservation_id) references reservations(id)
);