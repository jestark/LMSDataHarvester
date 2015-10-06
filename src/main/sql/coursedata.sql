-- Schema for the anonymized, "course" database of course data.  This schema
-- is documented in the wiki.

--****************************************************************************--
-- Course table
--****************************************************************************--

create table if not exists course_semester (
	id integer primary key,
	name text unique not null
);

comment on table course_semester is 'The Semester of offering of the course.  Acts as an Enum with the Values: WINTER, SPRING, and FALL';

create table if not exists course (
	id bigserial primary key,
	semester integer not null references course_semester (id) on delete restrict on update cascade,
	year integer not null,
	name text not null,
	unique (year, semester, name)
);

comment on table course is 'All of the course offerings';
comment on column course.semester  is 'Reference to the Semester in which the course was offered';
comment on column course.year is 'Year in which the course was offered, four digits (ie. 2014)';

--****************************************************************************--
-- Core Activity tables
--****************************************************************************--

-- The sources of the activity data.
create table if not exists activity_source (
	id bigserial primary key,
	name text unique not null
);

comment on table activity_source is 'The source of the activity data';

-- All of the components of the courses, from moodle and otherwise
create table if not exists activity_type (
	id bigserial primary key,
	source_id bigint not null references activity_source (id) on delete restrict on update cascade,
	name text not null,
	unique (source_id, name)
);

comment on table activity_type is 'The type of activity/module';
comment on column activity_type.name is 'Name of the Activity type, for moodle activities it must match the module name';

-- All of the instances of the various components of a course
create table if not exists activity (
	id bigserial primary key,
	course_id bigint not null references course (id) on delete restrict on update cascade,
	activity_type_id bigint not null references activity_type (id) on delete restrict on update cascade
);

comment on table activity is 'All of the activities associated with a course offering';

create table if not exists activity_stealth (
	activity_id bigint primary key references activity (id) on delete restrict on update cascade
);

comment on table activity_stealth is 'Activities found in the log but not added to the course by the instructor';

--****************************************************************************--
-- Enrolment tables
--****************************************************************************--

-- All of the roles that a participant could have in a course
create table if not exists enrolment_role (
	id bigserial primary key,
	name text unique not null
);

comment on table enrolment_role is 'All of the possible roles in a course';

-- All of the participants in a given course, listed with their role (student, ta, etc)
create table if not exists enrolment (
	id bigint primary key,
	course_id bigint not null references course (id) on delete restrict on update cascade,
	role_id bigint not null references enrolment_role (id) on delete restrict on update cascade,
	grade integer,
	usable boolean default false
);

comment on table enrolment is 'Anonymized list of all of the participants in a given course.';
comment on column enrolment.id is 'Randomized ID to protect the identity of the user';
comment on column enrolment.usable is 'True if the participant consented to their data being being used for research, false otherwise (Default: False)';
comment on column enrolment.grade is 'Final grade in the course';

-- All of the grades recorded for the gradable activities
create table if not exists enrolment_activity_grade (
	enrolment_id bigint not null references enrolment (id) on delete cascade on update cascade,
	activity_id bigint not null references activity (id) on delete restrict on update cascade,
	grade integer not null,
	primary key (enrolment_id, activity_id)
);

comment on table enrolment_activity_grade is 'Grades assigned to each student for a graded activity';

--****************************************************************************--
-- Core log tables
--****************************************************************************--

-- All of the possible actions, that can be logged.
create table if not exists log_action (
	id bigserial primary key,
	name text unique not null
);

comment on table log_action is 'All of the actions that can be performed on the activities.';

-- IP network associated with the log event
create table if not exists log_network (
	id bigserial primary key,
	name text unique not null
);

comment on table log_network is 'The IP network from which the logged action originated';

-- The log.
create table if not exists log (
	id bigserial primary key,
	enrolment_id bigint not null references enrolment (id) on delete cascade on update cascade,
	activity_id bigint not null references activity (id) on delete restrict on update cascade,
	action_id bigint not null references log_action (id) on delete restrict on update cascade,
	network_id bigint not null references log_network (id) on delete restrict on update cascade,
	time timestamp with time zone not null
);

comment on table log is 'Log of actions taken by participants in a given course';

--****************************************************************************--
-- Activity data tables for Moodle modules
--****************************************************************************--

-- Moodle assign module
create table if not exists activity_moodle_assign (
	activity_id bigint primary key references activity (id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_assign is 'Data from the moodle assign module';

-- Moodle Book Module
create table if not exists activity_moodle_book (
	activity_id bigint primary key references activity (id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_book is 'Data from the moodle book module';

create table if not exists activity_moodle_book_chapter (
	id bigserial primary key,
	book_id bigint not null references activity_moodle_book (activity_id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_book_chapter is 'Data for chapters in the moodle book module';

create table if not exists log_moodle_book_chapter (
	log_id bigint primary key references log (id) on delete cascade on update cascade,
	chapter_id bigint references activity_moodle_book_chapter (id) on delete restrict on update cascade
);

comment on table log_moodle_book_chapter is 'Relationship table for mapping log entries to moodle book chapters';

-- Moodle Checklist Module
create table if not exists activity_moodle_checklist (
	activity_id bigint primary key references activity (id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_checklist is 'Data from the moodle checklist module';

-- Moodle Choice Module
create table if not exists activity_moodle_choice (
	activity_id bigint primary key references activity (id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_choice is 'Data from the moodle choice module';

-- Moodle Feedback Module
create table if not exists activity_moodle_feedback (
	activity_id bigint primary key references activity (id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_feedback is 'Data from the moodle feedback module';

-- Moodle Folder Module
create table if not exists activity_moodle_folder (
	activity_id bigint primary key references activity (id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_folder is 'Data from the moodle folder module';

-- Moodle Forum Module
create table if not exists activity_moodle_forum (
	activity_id bigint primary key references activity (id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_forum is 'Data from the moodle forum module';

create table if not exists activity_moodle_forum_discussion (
	id bigserial primary key,
	forum_id bigint not null references activity_moodle_forum (activity_id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_forum_discussion is 'Data from moodle forum discussions';

create table if not exists log_moodle_forum_discussion (
	log_id bigint primary key references log (id) on delete cascade on update cascade,
	discussion_id bigint references activity_moodle_forum_discussion (id) on delete restrict on update cascade
);

comment on table log_moodle_forum_discussion is 'Relationship table for mapping log entries to moodle forum discussions';

create table if not exists activity_moodle_forum_post (
	id bigserial primary key,
	discussion_id bigint not null references activity_moodle_forum_discussion (id) on delete restrict on update cascade,
	subject text not null
);

comment on table activity_moodle_forum_post is 'Data from moodle forum posts';

create table if not exists log_moodle_forum_post (
	log_id bigint primary key references log (id) on delete cascade on update cascade,
	post_id bigint references activity_moodle_forum_post (id) on delete restrict on update cascade
);

comment on table log_moodle_forum_post is 'Relationship table for mapping log entries to moodle forum posts';

-- Moodle Label Module
create table if not exists activity_moodle_label (
	activity_id bigint primary key references activity (id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_label is 'Data from the moodle label module';

-- Moodle Lesson Module
create table if not exists activity_moodle_lesson (
	activity_id bigint primary key references activity (id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_lesson is 'Data from the moodle lesson module';

create table if not exists activity_moodle_lesson_page (
	id bigserial primary key,
	lesson_id bigint not null references activity_moodle_lesson (activity_id) on delete restrict on update cascade,
	title text not null
);

comment on table activity_moodle_lesson_page is 'Data from pages in the moodle lesson module';

create table if not exists log_moodle_lesson_page (
	log_id bigint primary key references log (id) on delete cascade on update cascade,
	page_id bigint references activity_moodle_lesson_page (id) on delete restrict on update cascade
);

comment on table log_moodle_lesson_page is 'Relationship table for mapping log entries to moodle lesson pages';

-- Moodle Page Module
create table if not exists activity_moodle_page (
	activity_id bigint primary key references activity (id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_page is 'Data from the moodle page module';

-- Moodle Quiz Module
create table if not exists activity_moodle_quiz (
	activity_id bigint primary key references activity (id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_quiz is 'Data from the moodle quiz module';

-- Moodle Resource Module
create table if not exists activity_moodle_resource (
	activity_id bigint primary key references activity (id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_resource is 'Data from the moodle resource module';

-- Moodle Scheduler Module
create table if not exists activity_moodle_scheduler (
	activity_id bigint primary key references activity (id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_scheduler is 'Data from the moodle scheduler module';

-- Moodle url Module
create table if not exists activity_moodle_url (
	activity_id bigint primary key references activity (id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_url is 'Data from the moodle url module';

-- Moodle Wiki Module
create table if not exists activity_moodle_wiki (
	activity_id bigint primary key references activity (id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_wiki is 'Data from the moodle wiki module';

create table if not exists activity_moodle_wiki_page (
	id bigserial primary key,
	wiki_id bigint not null references activity_moodle_wiki (activity_id) on delete restrict on update cascade,
	title text not null
);

comment on table activity_moodle_wiki_page is 'Data from pages in the moodle wiki module';

create table if not exists log_moodle_wiki_page (
	log_id bigint primary key references log (id) on delete cascade on update cascade,
	page_id bigint references activity_moodle_wiki_page (id) on delete restrict on update cascade
);

comment on table log_moodle_wiki_page is 'Relationship table for mapping log entries to moodle wiki pages';

-- Moodle Workshop Module
create table if not exists activity_moodle_workshop (
	activity_id bigint primary key references activity (id) on delete restrict on update cascade,
	name text not null
);

comment on table activity_moodle_workshop is 'Data from the moodle workshop module';

create table if not exists activity_moodle_workshop_submission (
	id bigserial primary key,
	workshop_id bigint not null references activity_moodle_workshop (activity_id) on delete restrict on update cascade,
	title text not null
);

comment on table activity_moodle_workshop_submission is 'Data from submissions in the moodle workshop module';

create table if not exists log_moodle_workshop_submission (
	log_id bigint primary key references log (id) on delete cascade on update cascade,
	submission_id bigint references activity_moodle_workshop_submission (id) on delete restrict on update cascade
);

comment on table log_moodle_workshop_submission is 'Relationship table for mapping log entries to moodle workshop submissions';

--****************************************************************************--
--  List of Known Modules (Activities)
--****************************************************************************--

insert into course_semester values (0, 'WINTER');
insert into course_semester values (1, 'SPRING');
insert into course_semester values (2, 'FALL');

insert into enrolment_role values (0, 'admin');

insert into activity_source (name) values ('moodle');

insert into log_network values (0, 'NONE');

insert into activity_type (source_id, name) select id, 'blog' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'calendar' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'course' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'grade' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'notes' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'role' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'user' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'assign' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'book' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'checklist' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'choice' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'feedback' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'folder' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'forum' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'label' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'lesson' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'page' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'quiz' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'resource' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'scheduler' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'url' from activity_source where name='moodle';
insert into activity_type (source_id, name) select id, 'workshop' from activity_source where name='moodle';
