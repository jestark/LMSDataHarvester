-- Schema for the anonymized, "course" database of course data.  This schema
-- is documented in the wiki.

--****************************************************************************--
-- Course table
--****************************************************************************--

create table if not exists course_semester (
	id integer primary key,
	name text unique not null
);

create table if not exists course (
	id bigserial primary key,
	semester integer not null references course_semester (id) on delete restrict on update cascade,
	year integer not null,
	name text not null,
	unique (year, semester, name)
);

comment on table course_semester is 'The Semester of offering of the course.  Acts as an Enum with the Values: WINTER, SPRING, and FALL';
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

-- All of the components of the courses, from moodle and otherwise
create table if not exists activity (
	id bigserial primary key,
	source_id bigint not null references activity_source (id) on delete restrict on update cascade,
	name text not null,
	unique (source_id, name)
);

-- All of the possible actions, that can be logged.
create table if not exists activity_action (
	id bigserial primary key,
	name text unique not null
);

-- Relationship table for the many-to-many mapping between activity and activity_action
create table if not exists activity_action_map (
	activity_id bigint not null references activity (id) on delete cascade on update cascade,
	action_id bigint not null references activity_action (id) on delete restrict on update cascade,
	primary key (activity_id, action_id)
);

-- All of the instances of the various components of a course
create table if not exists activity_instance (
	id bigserial primary key,
	course_id bigint not null references course (id) on delete restrict on update cascade,
	activity_id bigint not null references activity (id) on delete restrict on update cascade
);

create table if not exists activity_instance_stealth (
	instance_id bigint primary key references activity_instance (id) on delete restrict on update cascade
);

comment on table activity is 'The type of activity/module';
comment on table activity_source is 'The source of the activity data';
comment on table activity_action is 'All of the actions that can ber performed on the activities.';
comment on table activity_action_map is 'All of the actions associated with a given activity.';
comment on table activity_instance is 'All of the activities associated with a course offering';
comment on table activity_instance_stealth is 'Activities found in the log but not added to the course by the instructor';
comment on column activity.name is 'Name of the Activity type, for moodle activities it must match the module name';

--****************************************************************************--
-- Enrolment tables
--****************************************************************************--

-- All of the roles that a participant could have in a course
create table if not exists enrolment_role (
	id bigserial primary key,
	name text unique not null
);

-- All of the participants in a given course, listed with their role (student, ta, etc)
create table if not exists enrolment (
	id bigint primary key,
	course_id bigint not null references course (id) on delete restrict on update cascade,
	role_id bigint not null references enrolment_role (id) on delete restrict on update cascade,
	usable boolean default false
);

-- All of the recorded final grades
create table if not exists enrolment_final_grade (
	enrolment_id bigint primary key references enrolment (id) on delete cascade on update cascade,
	grade integer not null
);

-- All of the grades recorded for the gradable activities
create table if not exists enrolment_activity_grade (
	enrolment_id bigint not null references enrolment (id) on delete cascade on update cascade,
	instance_id bigint not null references activity_instance (id) on delete restrict on update cascade,
	grade integer not null,
	primary key (enrolment_id, instance_id)
);

comment on table enrolment is 'Anonymized list of all of the participants in a given course.';
comment on table enrolment_role is 'All of the possible roles in a course';
comment on table enrolment_final_grade is 'Final grades for all students that completed the course';
comment on table enrolment_activity_grade is 'Grades assigned to each student for a graded activity';
comment on column enrolment.id is 'Randomized ID to protect the identity of the user';
comment on column enrolment.usable is 'True if the participant consented to their data being being used for research, false otherwise (Default: False)';

--****************************************************************************--
-- Core log tables
--****************************************************************************--

-- IP network associated with the log event
create table if not exists log_network (
	id bigserial primary key,
	name text unique not null
);

-- The log.
create table if not exists log (
	id bigserial primary key,
	enrolment_id bigint not null references enrolment (id) on delete cascade on update cascade,
	instance_id bigint not null references activity_instance (id) on delete restrict on update cascade,
	action_id bigint not null references activity_action (id) on delete restrict on update cascade,
	network_id bigint not null references log_network (id) on delete restrict on update cascade,
	time timestamp with time zone not null
);

comment on table log is 'Log of actions taken by participants in a given course';
comment on table log_network is 'Th IP network from which the logged action originated';

--****************************************************************************--
-- Activity data tables for Moodle modules
--****************************************************************************--

-- Moodle assign module
create table if not exists activity_moodle_assign (
	instance_id bigint primary key references activity_instance (id) on delete restrict on update cascade,
	name text not null
);

-- Moodle Book Module
create table if not exists activity_moodle_book (
	instance_id bigint primary key references activity_instance (id) on delete restrict on update cascade,
	name text not null
);

create table if not exists activity_moodle_book_chapter (
	id bigserial primary key,
	book_id bigint not null references activity_moodle_book (instance_id) on delete restrict on update cascade,
	name text not null
);

-- Moodle Checklist Module
create table if not exists activity_moodle_checklist (
	instance_id bigint primary key references activity_instance (id) on delete restrict on update cascade,
	name text not null
);

-- Moodle Choice Module
create table if not exists activity_moodle_choice (
	instance_id bigint primary key references activity_instance (id) on delete restrict on update cascade,
	name text not null
);

-- Moodle Feedback Module
create table if not exists activity_moodle_feedback (
	instance_id bigint primary key references activity_instance (id) on delete restrict on update cascade,
	name text not null
);

-- Moodle Folder Module
create table if not exists activity_moodle_folder (
	instance_id bigint primary key references activity_instance (id) on delete restrict on update cascade,
	name text not null
);

-- Moodle Forum Module
create table if not exists activity_moodle_forum (
	instance_id bigint primary key references activity_instance (id) on delete restrict on update cascade,
	name text not null
);

create table if not exists activity_moodle_forum_discussion (
	id bigserial primary key,
	forum_id bigint not null references activity_moodle_forum (instance_id) on delete restrict on update cascade,
	name text not null
);

create table if not exists activity_moodle_forum_post (
	id bigserial primary key,
	discussion_id bigint not null references activity_moodle_forum_discussion (id) on delete restrict on update cascade,
	subject text not null
);

-- Moodle Label Module
create table if not exists activity_moodle_label (
	instance_id bigint primary key references activity_instance (id) on delete restrict on update cascade,
	name text not null
);

-- Moodle Lesson Module
create table if not exists activity_moodle_lesson (
	instance_id bigint primary key references activity_instance (id) on delete restrict on update cascade,
	name text not null
);

create table if not exists activity_moodle_lesson_page (
	id bigserial primary key,
	lesson_id bigint not null references activity_moodle_lesson (instance_id) on delete restrict on update cascade,
	title text not null
);

-- Moodle Page Module
create table if not exists activity_moodle_page (
	instance_id bigint primary key references activity_instance (id) on delete restrict on update cascade,
	name text not null
);

-- Moodle Quiz Module
create table if not exists activity_moodle_quiz (
	instance_id bigint primary key references activity_instance (id) on delete restrict on update cascade,
	name text not null
);

-- Moodle Resource Module
create table if not exists activity_moodle_resource (
	instance_id bigint primary key references activity_instance (id) on delete restrict on update cascade,
	name text not null
);

-- Moodle Scheduler Module
create table if not exists activity_moodle_scheduler (
	instance_id bigint primary key references activity_instance (id) on delete restrict on update cascade,
	name text not null
);

-- Moodle url Module
create table if not exists activity_moodle_url (
	instance_id bigint primary key references activity_instance (id) on delete restrict on update cascade,
	name text not null
);

-- Moodle Workshop Module
create table if not exists activity_moodle_workshop (
	instance_id bigint primary key references activity_instance (id) on delete restrict on update cascade,
	name text not null
);

create table if not exists activity_moodle_workshop_submission (
	id bigserial primary key,
	workshop_id bigint not null references activity_moodle_workshop (instance_id) on delete restrict on update cascade,
	title text not null
);

comment on table activity_moodle_assign is 'Data from the moodle assign module';
comment on table activity_moodle_book is 'Data from the moodle book module';
comment on table activity_moodle_book_chapter is 'Data for chapters in the moodle book module';
comment on table activity_moodle_checklist is 'Data from the moodle checklist module';
comment on table activity_moodle_choice is 'Data from the moodle choice module';
comment on table activity_moodle_feedback is 'Data from the moodle feedback module';
comment on table activity_moodle_folder is 'Data from the moodle folder module';
comment on table activity_moodle_forum is 'Data from the moodle forum module';
comment on table activity_moodle_forum_discussion is 'Data from moodle forum discussions';
comment on table activity_moodle_forum_post is 'Data from moodle forum posts';
comment on table activity_moodle_label is 'Data from the moodle label module';
comment on table activity_moodle_lesson is 'Data from the moodle lesson module';
comment on table activity_moodle_lesson_page is 'Data from pages in the moodle lesson module';
comment on table activity_moodle_page is 'Data from the moodle page module';
comment on table activity_moodle_quiz is 'Data from the moodle quiz module';
comment on table activity_moodle_resource is 'Data from the moodle resource module';
comment on table activity_moodle_scheduler is 'Data from the moodle scheduler module';
comment on table activity_moodle_url is 'Data from the moodle url module';
comment on table activity_moodle_workshop is 'Data from the moodle workshop module';
comment on table activity_moodle_workshop_submission is 'Data from submissions in the moodle workshop module';

--****************************************************************************--
-- Log reference tables for Moodle modules.
--****************************************************************************--

-- Moodle book module
create table if not exists log_moodle_book_chapter (
	log_id bigint primary key references log (id) on delete cascade on update cascade,
	chapter_id bigint references activity_moodle_book_chapter (id) on delete restrict on update cascade
);

-- Moodle forum module
create table if not exists log_moodle_forum_discussion (
	log_id bigint primary key references log (id) on delete cascade on update cascade,
	discussion_id bigint references activity_moodle_forum_discussion (id) on delete restrict on update cascade
);

create table if not exists log_moodle_forum_post (
	log_id bigint primary key references log (id) on delete cascade on update cascade,
	post_id bigint references activity_moodle_forum_post (id) on delete restrict on update cascade
);

-- Moodle lesson module
create table if not exists log_moodle_lesson_page (
	log_id bigint primary key references log (id) on delete cascade on update cascade,
	page_id bigint references activity_moodle_lesson_page (id) on delete restrict on update cascade
);

-- Moodle workshop module
create table if not exists log_moodle_workshop_submission (
	log_id bigint primary key references log (id) on delete cascade on update cascade,
	submission_id bigint references activity_moodle_workshop_submission (id) on delete restrict on update cascade
);

comment on table log_moodle_book_chapter is 'Relationship table for mapping log entries to moodle book chapters';
comment on table log_moodle_forum_discussion is 'Relationship table for mapping log entries to moodle forum discussions';
comment on table log_moodle_forum_post is 'Relationship table for mapping log entries to moodle forum posts';
comment on table log_moodle_lesson_page is 'Relationship table for mapping log entries to moodle lesson pages';
comment on table log_moodle_workshop_submission is 'Relationship table for mapping log entries to moodle workshop submissions';

--****************************************************************************--
--  List of Known Modules (Activities)
--****************************************************************************--

insert into course_semester values (0, 'WINTER');
insert into course_semester values (1, 'SPRING');
insert into course_semester values (2, 'FALL');

insert into activity_source (name) values ('moodle');

insert into log_network values (0, "NONE");

insert into activity (source_id, name) select id, 'blog' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'calendar' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'course' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'grade' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'notes' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'role' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'user' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'assign' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'book' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'checklist' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'choice' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'feedback' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'folder' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'forum' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'label' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'lesson' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'page' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'quiz' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'resource' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'scheduler' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'url' from activity_source where name='moodle';
insert into activity (source_id, name) select id, 'workshop' from activity_source where name='moodle';
