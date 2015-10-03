-- Schema for the private database.  Documentation for the schema is on the
-- wiki.

create schema if not exists userdb;

-- Identifying information for students in the public database.
create table if not exists userdb.user (
	id bigserial primary key,
	username text not null unique,
	first_name text not null,
	last_name text not null
);

create table if not exists userdb.user_enrolment (
	enrolment_id bigint primary key references enrolment (id) on delete cascade on update cascade,
	user_id bigint not null references userdb.user (id) on delete cascade on update cascade
);

comment on schema userdb is 'Restricted access to identifying user information';
comment on table userdb.user is 'Identifying user data';
comment on table userdb.user_enrolment is 'User to enrolment mapping';

insert into userdb.user values (0, '@@NONE@@', 'NULL', 'USER');
insert into userdb.user (username, first_name, last_name) values ('admin', 'moodle', 'administrator');
