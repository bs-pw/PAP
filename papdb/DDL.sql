

DROP TABLE attendance_statuses CASCADE CONSTRAINTS;

DROP TABLE attendances CASCADE CONSTRAINTS;

DROP TABLE class_types CASCADE CONSTRAINTS;

DROP TABLE classes CASCADE CONSTRAINTS;

DROP TABLE coordinators CASCADE CONSTRAINTS;

DROP TABLE courses CASCADE CONSTRAINTS;

DROP TABLE courses_in_semester CASCADE CONSTRAINTS;

DROP TABLE final_grades CASCADE CONSTRAINTS;

DROP TABLE grade_categories CASCADE CONSTRAINTS;

DROP TABLE grades CASCADE CONSTRAINTS;

DROP TABLE groups CASCADE CONSTRAINTS;

DROP TABLE lecturers CASCADE CONSTRAINTS;

DROP TABLE semesters CASCADE CONSTRAINTS;

DROP TABLE students_in_groups CASCADE CONSTRAINTS;

DROP TABLE user_types CASCADE CONSTRAINTS;

DROP TABLE users CASCADE CONSTRAINTS;


CREATE TABLE attendance_statuses (
    attendance_status_id NUMBER(6) NOT NULL,
    status               VARCHAR2(32 CHAR) NOT NULL
);

ALTER TABLE attendance_statuses ADD CONSTRAINT attendance_statuses_pk PRIMARY KEY ( attendance_status_id );

CREATE TABLE attendances (
    user_id                           NUMBER(6) NOT NULL,
    course_code                     VARCHAR2(5 CHAR) NOT NULL,
    semester                    VARCHAR2(4 CHAR) NOT NULL,
    group_number                     NUMBER(3) NOT NULL,
    class_id_for_group               NUMBER(3) NOT NULL,
    "date"                                   DATE NOT NULL,
    who_inserted_id                            NUMBER(6), 
    attendance_status_id NUMBER(6)
);

ALTER TABLE attendances
    ADD CONSTRAINT attendances_pk PRIMARY KEY ( user_id,
                                                course_code,
                                                group_number,
                                                class_id_for_group,
                                                "date",
                                                semester );

CREATE TABLE class_types (
    class_type_id NUMBER(3) NOT NULL,
    type          VARCHAR2(32 CHAR) NOT NULL
);

ALTER TABLE class_types ADD CONSTRAINT class_types_pk PRIMARY KEY ( class_type_id );

CREATE TABLE classes (
    course_code       VARCHAR2(5 CHAR) NOT NULL,
    group_number       NUMBER(3)  GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    semester      VARCHAR2(4 CHAR) NOT NULL,
    class_id_for_group        NUMBER(3) NOT NULL,
    day                       NUMBER(2) NOT NULL,
    hour                      NUMBER(4) NOT NULL,
    length                    NUMBER(4) NOT NULL,
    "where"                   VARCHAR2(32 CHAR) NOT NULL,
    class_type_id NUMBER(3)
);

ALTER TABLE classes
    ADD CONSTRAINT classes_pk PRIMARY KEY ( class_id_for_group,
                                            group_number,
                                            course_code,
                                            semester );

CREATE TABLE coordinators (
    user_id                     NUMBER(6) NOT NULL, 
    course_code  VARCHAR2(5 CHAR) NOT NULL, 
    semester VARCHAR2(4 CHAR) NOT NULL
);

ALTER TABLE coordinators
    ADD CONSTRAINT coordinators_pk PRIMARY KEY ( user_id,
                                                 course_code,
                                                 semester );

CREATE TABLE courses (
    course_code VARCHAR2(5 CHAR) NOT NULL,
    title        VARCHAR2(64 CHAR) NOT NULL
);

ALTER TABLE courses ADD CONSTRAINT courses_pk PRIMARY KEY ( course_code );

CREATE TABLE courses_in_semester (
    course_code    VARCHAR2(5 CHAR) NOT NULL,
    semester VARCHAR2(4 CHAR) NOT NULL
);

ALTER TABLE courses_in_semester ADD CONSTRAINT courses_in_semester_pk PRIMARY KEY ( course_code,
                                                                                    semester );

CREATE TABLE final_grades (
    user_id                            NUMBER(6) NOT NULL, 
    course_code VARCHAR2(5 CHAR) NOT NULL, 
    semester        VARCHAR2(4 CHAR) NOT NULL,
    grade                                    NUMBER(2, 1) NOT NULL
);

ALTER TABLE final_grades
    ADD CONSTRAINT final_grades_pk PRIMARY KEY ( user_id,
                                                 course_code,
                                                 semester );

CREATE TABLE grade_categories (
    category_id                              NUMBER(6) NOT NULL, 
    course_code VARCHAR2(5 CHAR) NOT NULL, 
    semester        VARCHAR2(4 CHAR) NOT NULL,
    description                              VARCHAR2(128 CHAR),
    max_grade                                NUMBER(6, 2) NOT NULL
);

ALTER TABLE grade_categories
    ADD CONSTRAINT grade_categories_pk PRIMARY KEY ( category_id,
                                                     course_code,
                                                     semester );

CREATE TABLE grades (
    category_id                              NUMBER(6) NOT NULL, 
    course_code VARCHAR2(5 CHAR) NOT NULL, 
    semester        VARCHAR2(4 CHAR) NOT NULL,
    user_id                                             NUMBER(6) NOT NULL,
    who_inserted_id                                            NUMBER(6),
    grade                                                     NUMBER(5, 2) NOT NULL,
    "date"                                                    DATE NOT NULL,
    description                                               VARCHAR2(128 CHAR)
);

ALTER TABLE grades
    ADD CONSTRAINT grades_pk PRIMARY KEY ( course_code,
                                           category_id,
                                           user_id,
                                           semester );

CREATE TABLE groups ( 
    course_code VARCHAR2(5 CHAR) NOT NULL,
    group_number                             NUMBER(3) NOT NULL, 
    semester        VARCHAR2(4 CHAR) NOT NULL
);

ALTER TABLE groups
    ADD CONSTRAINT groups_pk PRIMARY KEY ( course_code,
                                           group_number,
                                           semester );

CREATE TABLE lecturers (
    user_id                                   NUMBER(6) NOT NULL, 
    course_code VARCHAR2(5 CHAR) NOT NULL, 
    semester        VARCHAR2(4 CHAR) NOT NULL,
    group_number                             NUMBER(3) NOT NULL
);

ALTER TABLE lecturers
    ADD CONSTRAINT lecturers_pk PRIMARY KEY ( user_id,
                                              course_code,
                                              group_number,
                                              semester );

CREATE TABLE semesters (
    semester_code VARCHAR2(4 CHAR) NOT NULL,
    start_date    DATE NOT NULL,
    end_date      DATE NOT NULL
);

ALTER TABLE semesters ADD CONSTRAINT semesters_pk PRIMARY KEY ( semester_code );

CREATE TABLE students_in_groups (
    user_id                                   NUMBER(6) NOT NULL, 
    course_code VARCHAR2(5 CHAR) NOT NULL, 
    semester        VARCHAR2(4 CHAR) NOT NULL,
    group_number                             NUMBER(3) NOT NULL
);

ALTER TABLE students_in_groups
    ADD CONSTRAINT students_in_groups_pk PRIMARY KEY ( user_id,
                                                       course_code,
                                                       group_number,
                                                       semester );

CREATE TABLE user_types (
    user_type_id NUMBER(3) NOT NULL,
    type         VARCHAR2(32 CHAR) NOT NULL
);

ALTER TABLE user_types ADD CONSTRAINT user_types_pk PRIMARY KEY ( user_type_id );

CREATE TABLE users (
    user_id                 NUMBER(6)  GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    name                    VARCHAR2(64 CHAR) NOT NULL,
    surname                 VARCHAR2(64 CHAR) NOT NULL,
    password                VARCHAR2(64 CHAR) NOT NULL,
    mail                    VARCHAR2(64 CHAR) NOT NULL,
    user_type_id NUMBER(3)
);

ALTER TABLE users ADD CONSTRAINT users_pk PRIMARY KEY ( user_id );

ALTER TABLE attendances
    ADD CONSTRAINT attendances_attendance_statuses_fk FOREIGN KEY ( attendance_status_id )
        REFERENCES attendance_statuses ( attendance_status_id );

ALTER TABLE attendances
    ADD CONSTRAINT attendances_classes_fk FOREIGN KEY ( class_id_for_group,
                                                        group_number,
                                                        course_code,
                                                        semester )
        REFERENCES classes ( class_id_for_group,
                             group_number,
                             course_code,
                             semester );

ALTER TABLE attendances
    ADD CONSTRAINT attendances_users_fk FOREIGN KEY ( who_inserted_id )
        REFERENCES users ( user_id );

ALTER TABLE attendances
    ADD CONSTRAINT attendances_users_fkv2 FOREIGN KEY ( user_id )
        REFERENCES users ( user_id );

ALTER TABLE classes
    ADD CONSTRAINT classes_class_types_fk FOREIGN KEY ( class_type_id )
        REFERENCES class_types ( class_type_id );

ALTER TABLE classes
    ADD CONSTRAINT classes_groups_fk FOREIGN KEY ( course_code,
                                                   group_number,
                                                   semester )
        REFERENCES groups ( course_code,
                            group_number,
                            semester );

ALTER TABLE coordinators
    ADD CONSTRAINT coordinators_courses_in_semester_fk FOREIGN KEY ( course_code,
                                                                     semester )
        REFERENCES courses_in_semester ( course_code,
                                         semester );

ALTER TABLE coordinators
    ADD CONSTRAINT coordinators_users_fk FOREIGN KEY ( user_id )
        REFERENCES users ( user_id );

ALTER TABLE courses_in_semester
    ADD CONSTRAINT courses_in_semester_courses_fk FOREIGN KEY ( course_code )
        REFERENCES courses ( course_code );

ALTER TABLE courses_in_semester
    ADD CONSTRAINT courses_in_semester_semesters_fk FOREIGN KEY ( semester )
        REFERENCES semesters ( semester_code );

ALTER TABLE final_grades
    ADD CONSTRAINT final_grades_courses_in_semester_fk FOREIGN KEY ( course_code,
                                                                     semester )
        REFERENCES courses_in_semester ( course_code,
                                         semester );

ALTER TABLE final_grades
    ADD CONSTRAINT final_grades_users_fk FOREIGN KEY ( user_id )
        REFERENCES users ( user_id );

ALTER TABLE grade_categories
    ADD CONSTRAINT grade_categories_courses_in_semester_fk FOREIGN KEY ( course_code,
                                                                         semester )
        REFERENCES courses_in_semester ( course_code,
                                         semester );

ALTER TABLE grades
    ADD CONSTRAINT grades_grade_categories_fk FOREIGN KEY ( category_id,
                                                            course_code,
                                                            semester )
        REFERENCES grade_categories ( category_id,
                                      course_code,
                                      semester );

ALTER TABLE grades
    ADD CONSTRAINT grades_users_fk FOREIGN KEY ( user_id )
        REFERENCES users ( user_id );

ALTER TABLE grades
    ADD CONSTRAINT grades_users_fkv2 FOREIGN KEY ( who_inserted_id )
        REFERENCES users ( user_id );

ALTER TABLE groups
    ADD CONSTRAINT groups_courses_in_semester_fk FOREIGN KEY ( course_code,
                                                               semester )
        REFERENCES courses_in_semester ( course_code,
                                         semester );

ALTER TABLE lecturers
    ADD CONSTRAINT lecturers_groups_fk FOREIGN KEY ( course_code,
                                                     group_number,
                                                     semester )
        REFERENCES groups ( course_code,
                            group_number,
                            semester );

ALTER TABLE lecturers
    ADD CONSTRAINT lecturers_users_fk FOREIGN KEY ( user_id )
        REFERENCES users ( user_id );

ALTER TABLE students_in_groups
    ADD CONSTRAINT students_in_groups_groups_fk FOREIGN KEY ( course_code,
                                                              group_number,
                                                              semester )
        REFERENCES groups ( course_code,
                            group_number,
                            semester );

ALTER TABLE students_in_groups
    ADD CONSTRAINT students_in_groups_users_fk FOREIGN KEY ( user_id )
        REFERENCES users ( user_id );

ALTER TABLE users
    ADD CONSTRAINT users_user_types_fk FOREIGN KEY ( user_type_id )
        REFERENCES user_types ( user_type_id );


-- CONSTRAINTS

ALTER TABLE users ADD CONSTRAINT mail_const UNIQUE ( mail );

ALTER TABLE final_grades ADD CONSTRAINT grade_const CHECK ( grade >= 0 AND grade <= 5 AND MOD(grade, 0.5) = 0 );  