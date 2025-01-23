-- TRIGGERS
create or replace trigger check_grade_trigger before
   insert or update on grades
   for each row
declare
   v_max_grade grade_categories.max_grade%type;
begin
   select max_grade
     into v_max_grade
     from grade_categories
    where category_id = :new.category_id;

   if :new.grade > v_max_grade
   or :new.grade < 0 then
      raise_application_error(
         -20001,
         'Grade cannot be greater than the max grade for this category and lower than 0.'
      );
   end if;
end;
/
alter trigger check_grade_trigger enable;


create or replace trigger check_grade_category_trigger before
   insert or update on grade_categories
   for each row
declare
   v_max_grade grades.grade%type;
begin
   select max(grade)
     into v_max_grade
     from grades
    where category_id = :new.category_id;

   if :new.max_grade < v_max_grade
   or :new.max_grade <= 0 then
      raise_application_error(
         -20001,
         'Max grade cannot be lower than the maximum grade for this category and than 0.'
      );
   end if;
end;
/
alter trigger check_grade_category_trigger enable;



create or replace trigger check_semester_trigger before
   insert or update on semesters
   for each row
declare begin
   for r_sem in (
      select *
        from semesters
       where :old.semester_code != semester_code
   ) loop
      if :new.start_date between r_sem.start_date and r_sem.end_date
      or :new.end_date between r_sem.start_date and r_sem.end_date then
         raise_application_error(
            -20001,
            'Semester cannot interfere other semesters!'
         );
      end if;
   end loop;
end;
/
alter trigger check_semester_trigger enable;

create or replace trigger close_semester_trigger before
   update of is_closed on courses_in_semester
   for each row
declare begin
   update final_grades
      set
      grade = 2.0
    where semester = :old.semester
      and course_code = :old.course_code
      and grade is null;
end;
/
alter trigger close_semester_trigger enable;

-- FUNCTIONS

create or replace function days_untill_end_of_semester (
   p_semester_code semesters.semester_code%type
) return number as
   v_end_date semesters.end_date%type;
begin
   select end_date
     into v_end_date
     from semesters
    where semester_code = p_semester_code;

   return floor(v_end_date - sysdate);
end;
/

create or replace function get_current_semester return semesters.semester_code%type as
   v_current_semester semesters.semester_code%type;
begin
   select semester_code
     into v_current_semester
     from semesters
    where start_date <= sysdate
      and end_date >= sysdate;

   return v_current_semester;
end;
/

-- PROCEDURES
create or replace procedure give_final_grades_from_points (
   p_semester    semesters.semester_code%type,
   p_course_code courses.course_code%type
) as
   students_points number(
      6,
      2
   );
   max_points      number(
      6,
      2
   );
begin
   for r_user in (
      select user_id
        from final_grades
       where semester = p_semester
         and course_code = p_course_code
         and grade is null
   ) loop
      select sum(grade)
        into students_points
        from grades
       where user_id = r_user.user_id
         and semester = p_semester
         and course_code = p_course_code;

      select sum(max_grade)
        into max_points
        from grade_categories
       where semester = p_semester
         and course_code = p_course_code;

      if students_points / max_points >= 0.5 then
         if students_points / max_points >= 0.6 then
            if students_points / max_points >= 0.7 then
               if students_points / max_points >= 0.8 then
                  if students_points / max_points >= 0.9 then
                     update final_grades
                        set
                        grade = 5.0
                      where user_id = r_user.user_id
                        and semester = p_semester
                        and course_code = p_course_code;
                  else
                     update final_grades
                        set
                        grade = 4.5
                      where user_id = r_user.user_id
                        and semester = p_semester
                        and course_code = p_course_code;
                  end if;
               else
                  update final_grades
                     set
                     grade = 4.0
                   where user_id = r_user.user_id
                     and semester = p_semester
                     and course_code = p_course_code;
               end if;
            else
               update final_grades
                  set
                  grade = 3.5
                where user_id = r_user.user_id
                  and semester = p_semester
                  and course_code = p_course_code;
            end if;
         else
            update final_grades
               set
               grade = 3.0
             where user_id = r_user.user_id
               and semester = p_semester
               and course_code = p_course_code;
         end if;
      else
         update final_grades
            set
            grade = 2.0
          where user_id = r_user.user_id
            and semester = p_semester
            and course_code = p_course_code;
      end if;
   end loop;
end;
/


create or replace procedure inactivate_users (
   p_semester_code semesters.semester_code%type
) as
begin
   update users
      set
      user_type_id = 4
    where user_type_id != 0
      and user_id not in (
      select user_id
        from final_grades
       where semester = p_semester_code
   )
      and user_id not in (
      select user_id
        from coordinators
       where semester = p_semester_code
   )
      and user_id not in (
      select user_id
        from lecturers
       where semester = p_semester_code
   );
end;