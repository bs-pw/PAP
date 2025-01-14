drop table attendance_statuses cascade constraints;

drop table attendances cascade constraints;

drop table class_types cascade constraints;

drop table classes cascade constraints;

drop table coordinators cascade constraints;

drop table courses cascade constraints;

drop table courses_in_semester cascade constraints;

drop table final_grades cascade constraints;

drop table grade_categories cascade constraints;

drop table grades cascade constraints;

drop table groups cascade constraints;

drop table lecturers cascade constraints;

drop table semesters cascade constraints;

drop table students_in_groups cascade constraints;

drop table user_types cascade constraints;

drop table users cascade constraints;


create table attendance_statuses (
   attendance_status_id number(6) not null,
   status               varchar2(32 char) not null
);

alter table attendance_statuses add constraint attendance_statuses_pk primary key ( attendance_status_id );

create table attendances (
   user_id              number(6) not null,
   course_code          varchar2(5 char) not null,
   semester             varchar2(4 char) not null,
   group_number         number(3) not null,
   class_id_for_group   number(3) not null,
   "date"               date not null,
   who_inserted_id      number(6),
   attendance_status_id number(6)
);

alter table attendances
   add constraint attendances_pk
      primary key ( user_id,
                    course_code,
                    group_number,
                    class_id_for_group,
                    "date",
                    semester );

create table class_types (
   class_type_id number(3) not null,
   type          varchar2(32 char) not null
);

alter table class_types add constraint class_types_pk primary key ( class_type_id );

create table classes (
   course_code        varchar2(5 char) not null,
   group_number       number(3) not null,
   semester           varchar2(4 char) not null,
   class_id_for_group number(3)
      generated always as identity ( start with 1 increment by 1 ),
   day                number(2) not null,
   hour               number(4) not null,
   length             number(4) not null,
   "where"            varchar2(32 char) not null,
   class_type_id      number(3) not null
);

alter table classes
   add constraint classes_pk
      primary key ( class_id_for_group,
                    group_number,
                    course_code,
                    semester );

create table coordinators (
   user_id     number(6) not null,
   course_code varchar2(5 char) not null,
   semester    varchar2(4 char) not null
);

alter table coordinators
   add constraint coordinators_pk primary key ( user_id,
                                                course_code,
                                                semester );

create table courses (
   course_code varchar2(5 char) not null,
   title       varchar2(64 char) not null
);

alter table courses add constraint courses_pk primary key ( course_code );

create table courses_in_semester (
   course_code varchar2(5 char) not null,
   semester    varchar2(4 char) not null
);

alter table courses_in_semester add constraint courses_in_semester_pk primary key ( course_code,
                                                                                    semester );

create table final_grades (
   user_id     number(6) not null,
   course_code varchar2(5 char) not null,
   semester    varchar2(4 char) not null,
   grade       number(2,1)
);

alter table final_grades
   add constraint final_grades_pk primary key ( user_id,
                                                course_code,
                                                semester );

create table grade_categories (
   category_id number(6) not null,
   course_code varchar2(5 char) not null,
   semester    varchar2(4 char) not null,
   description varchar2(128 char),
   max_grade   number(6,2) not null
);

alter table grade_categories
   add constraint grade_categories_pk primary key ( category_id,
                                                    course_code,
                                                    semester );

create table grades (
   category_id     number(6) not null,
   course_code     varchar2(5 char) not null,
   semester        varchar2(4 char) not null,
   user_id         number(6) not null,
   who_inserted_id number(6),
   grade           number(5,2) not null,
   "date"          date not null,
   description     varchar2(128 char)
);

alter table grades
   add constraint grades_pk
      primary key ( course_code,
                    category_id,
                    user_id,
                    semester );

create table groups (
   course_code  varchar2(5 char) not null,
   group_number number(3) not null,
   semester     varchar2(4 char) not null
);

alter table groups
   add constraint groups_pk primary key ( course_code,
                                          group_number,
                                          semester );

create table lecturers (
   user_id      number(6) not null,
   course_code  varchar2(5 char) not null,
   semester     varchar2(4 char) not null,
   group_number number(3) not null
);

alter table lecturers
   add constraint lecturers_pk
      primary key ( user_id,
                    course_code,
                    group_number,
                    semester );

create table semesters (
   semester_code varchar2(4 char) not null,
   start_date    date not null,
   end_date      date not null
);

alter table semesters add constraint semesters_pk primary key ( semester_code );

create table students_in_groups (
   user_id      number(6) not null,
   course_code  varchar2(5 char) not null,
   semester     varchar2(4 char) not null,
   group_number number(3) not null
);

alter table students_in_groups
   add constraint students_in_groups_pk
      primary key ( user_id,
                    course_code,
                    group_number,
                    semester );

create table user_types (
   user_type_id number(3) not null,
   type         varchar2(32 char) not null
);

alter table user_types add constraint user_types_pk primary key ( user_type_id );

create table users (
   user_id      number(6)
      generated always as identity ( start with 1 increment by 1 ),
   name         varchar2(64 char) not null,
   surname      varchar2(64 char) not null,
   password     varchar2(100 char) not null,
   mail         varchar2(64 char) not null,
   user_type_id number(3) not null
);

alter table users add constraint users_pk primary key ( user_id );

alter table attendances
   add constraint attendances_attendance_statuses_fk foreign key ( attendance_status_id )
      references attendance_statuses ( attendance_status_id );

alter table attendances
   add constraint attendances_classes_fk
      foreign key ( class_id_for_group,
                    group_number,
                    course_code,
                    semester )
         references classes ( class_id_for_group,
                              group_number,
                              course_code,
                              semester );

alter table attendances
   add constraint attendances_users_fk foreign key ( who_inserted_id )
      references users ( user_id );

alter table attendances
   add constraint attendances_final_grades_fk
      foreign key ( user_id,
                    course_code,
                    semester )
         references final_grades ( user_id,
                                   course_code,
                                   semester );

alter table classes
   add constraint classes_class_types_fk foreign key ( class_type_id )
      references class_types ( class_type_id );

alter table classes
   add constraint classes_groups_fk
      foreign key ( course_code,
                    group_number,
                    semester )
         references groups ( course_code,
                             group_number,
                             semester );

alter table coordinators
   add constraint coordinators_courses_in_semester_fk
      foreign key ( course_code,
                    semester )
         references courses_in_semester ( course_code,
                                          semester );

alter table coordinators
   add constraint coordinators_users_fk foreign key ( user_id )
      references users ( user_id );

alter table courses_in_semester
   add constraint courses_in_semester_courses_fk foreign key ( course_code )
      references courses ( course_code );

alter table courses_in_semester
   add constraint courses_in_semester_semesters_fk foreign key ( semester )
      references semesters ( semester_code );

alter table final_grades
   add constraint final_grades_courses_in_semester_fk
      foreign key ( course_code,
                    semester )
         references courses_in_semester ( course_code,
                                          semester );

alter table final_grades
   add constraint final_grades_users_fk foreign key ( user_id )
      references users ( user_id );

alter table grade_categories
   add constraint grade_categories_courses_in_semester_fk
      foreign key ( course_code,
                    semester )
         references courses_in_semester ( course_code,
                                          semester );

alter table grades
   add constraint grades_grade_categories_fk
      foreign key ( category_id,
                    course_code,
                    semester )
         references grade_categories ( category_id,
                                       course_code,
                                       semester );

alter table grades
   add constraint grades_users_fk foreign key ( who_inserted_id )
      references users ( user_id );

alter table grades
   add constraint grades_final_grades_fk
      foreign key ( user_id,
                    course_code,
                    semester )
         references final_grades ( user_id,
                                   course_code,
                                   semester );

alter table groups
   add constraint groups_courses_in_semester_fk
      foreign key ( course_code,
                    semester )
         references courses_in_semester ( course_code,
                                          semester );

alter table students_in_groups
   add constraint students_in_groups_final_grades_fk
      foreign key ( user_id,
                    course_code,
                    semester )
         references final_grades ( user_id,
                                   course_code,
                                   semester );

alter table lecturers
   add constraint lecturers_groups_fk
      foreign key ( course_code,
                    group_number,
                    semester )
         references groups ( course_code,
                             group_number,
                             semester );

alter table lecturers
   add constraint lecturers_users_fk foreign key ( user_id )
      references users ( user_id );

alter table students_in_groups
   add constraint students_in_groups_groups_fk
      foreign key ( course_code,
                    group_number,
                    semester )
         references groups ( course_code,
                             group_number,
                             semester );

alter table users
   add constraint users_user_types_fk foreign key ( user_type_id )
      references user_types ( user_type_id );


-- CONSTRAINTS

alter table users add constraint mail_const unique ( mail );

alter table final_grades
   add constraint grade_const
      check ( grade >= 0
         and grade <= 5
         and mod(
         grade,
         0.5
      ) = 0 );
alter table attendance_statuses add constraint unique_attendance_status unique ( status );
alter table class_types add constraint unique_class_type unique ( type );
alter table user_types add constraint unique_user_type unique ( type );