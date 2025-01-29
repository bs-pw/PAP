-- Testy triggerów
-- Inicjalizacja danych
declare
   v_user_id   users.user_id%type;
   v_grade_cat grade_categories.category_id%type;
begin
   insert into semesters values ( 'T24Z',
                                  to_date('2024-01-01','YYYY-MM-DD'),
                                  to_date('2024-01-31','YYYY-MM-DD') );
   insert into courses values ( 'TBD1',
                                'TBazy danych 1' );
   insert into courses_in_semester values ( 'TBD1',
                                            'T24Z',
                                            0 );

   insert into grade_categories (
      course_code,
      semester,
      description,
      max_grade
   ) values ( 'TBD1',
              'T24Z',
              'kolokwium',
              100 );
   select category_id
     into v_grade_cat
     from grade_categories
    where course_code = 'TBD1'
      and semester = 'T24Z'
      and description = 'kolokwium';
   begin
      insert into user_types values ( 3,
                                      'student' );
   exception
      when others then
         null;
   end;
   insert into users (
      name,
      surname,
      password,
      mail,
      user_type_id
   ) values ( 'Jan',
              'Kowalski',
              'testpass',
              'jkowaltest@pw.edu.pl',
              3 );
   select user_id
     into v_user_id
     from users
    where mail = 'jkowaltest@pw.edu.pl';
   insert into final_grades (
      course_code,
      semester,
      user_id
   ) values ( 'TBD1',
              'T24Z',
              v_user_id );
   insert into groups (
      course_code,
      semester,
      group_number
   ) values ( 'TBD1',
              'T24Z',
              103 );
   insert into students_in_groups (
      course_code,
      semester,
      group_number,
      user_id
   ) values ( 'TBD1',
              'T24Z',
              103,
              v_user_id );
-- Test triggera grades_check - dodawanie oceny powyżej maksymalnej w kategorii - zwraca błąd
   begin
      insert into grades (
         category_id,
         course_code,
         semester,
         user_id,
         grade,
         "date"
      ) values ( v_grade_cat,
                 'TBD1',
                 'T24Z',
                 v_user_id,
                 500,
                 sysdate );
      dbms_output.put_line('TEST NIEzdany! -> ' || sqlerrm);
   exception
      when others then
         dbms_output.put_line('TEST zdany! -> ' || sqlerrm);
   end;

-- Test triggera grades_check - dodawanie oceny poniżej 0  - zwraca błąd
   begin
      insert into grades (
         category_id,
         course_code,
         semester,
         user_id,
         grade,
         "date"
      ) values ( v_grade_cat,
                 'TBD1',
                 'T24Z',
                 v_user_id,
                 - 1,
                 sysdate );
      dbms_output.put_line('TEST NIEzdany! -> ' || sqlerrm);
   exception
      when others then
         dbms_output.put_line('TEST zdany! -> ' || sqlerrm);
   end;
-- Test triggera grades_check - dodawanie oceny w odpowiednim przedziale - bez błędu
   begin
      insert into grades (
         category_id,
         course_code,
         semester,
         user_id,
         grade,
         "date"
      ) values ( v_grade_cat,
                 'TBD1',
                 'T24Z',
                 v_user_id,
                 99,
                 sysdate );
      dbms_output.put_line('TEST zdany! -> ' || sqlerrm);
   exception
      when others then
         dbms_output.put_line('TEST NIEzdany! -> ' || sqlerrm);
   end;
-- Test triggera grade_categories_check - zmiana maksymalnej oceny na wartość mniejszą niż maksymalna ocena w danej kategorii - zwraca błąd
   begin
      update grade_categories
         set
         max_grade = 98
       where category_id = v_grade_cat
         and course_code = 'TBD1'
         and semester = 'T24Z';
      dbms_output.put_line('TEST NIEzdany! -> ' || sqlerrm);
   exception
      when others then
         dbms_output.put_line('TEST zdany! -> ' || sqlerrm);
   end;
   
-- Test triggera grade_categories_check - zmiana maksymalnej oceny na wartość większą niż maksymalna ocena w danej kategorii - bez błędu
   begin
      update grade_categories
         set
         max_grade = 101
       where category_id = v_grade_cat
         and course_code = 'TBD1'
         and semester = 'T24Z';
      dbms_output.put_line('TEST zdany! -> ' || sqlerrm);
   exception
      when others then
         dbms_output.put_line('TEST NIEzdany! -> ' || sqlerrm);
   end;
   -- Usunięcie danych
   delete grades
    where course_code = 'TBD1'
      and semester = 'T24Z';
   delete students_in_groups
    where course_code = 'TBD1'
      and semester = 'T24Z'
      and user_id = v_user_id;
   delete final_grades
    where course_code = 'TBD1'
      and semester = 'T24Z';
   delete grade_categories
    where course_code = 'TBD1'
      and semester = 'T24Z';
   delete groups
    where course_code = 'TBD1'
      and semester = 'T24Z';
   delete courses_in_semester
    where course_code = 'TBD1'
      and semester = 'T24Z';
   delete courses
    where course_code = 'TBD1';
   delete semesters
    where semester_code = 'T24Z';
   delete users
    where user_id = v_user_id;
    -- DELETE user_types where user_type_id = 3;

end;
/
commit;

--Dane wykorzystywane poniżej
begin
   insert into users values ( 201,
                              'Jan Testowy',
                              'AABBCCDD',
                              'jtest@pw.edu.pl',
                              3 );

   insert into groups (
      course_code,
      group_number,
      semester
   ) values ( 'PZSP1',
              101,
              '24Z' );
end;
/
--Zapisuję studenta na przedmiot. Próba wypisania studenta powinna zakończyć się sukcesem.
declare
   v_s_id   number := 201;
   v_c_code varchar2(5) := 'PZSP1';
   v_s_code varchar2(5) := '24Z';
   v_g_num  number := 101;
begin
   insert into final_grades values ( v_s_id,
                                     v_c_code,
                                     v_s_code,
                                     null );
   delete from final_grades
    where user_id = v_s_id
      and course_code = v_c_code
      and semester = v_s_code;
   dbms_output.put_line('Test zdany');
   rollback;
exception
   when others then
      dbms_output.put_line('TEST NIEzdany! -> ' || sqlerrm);
      rollback;
end;
/
--Zapisuję studenta na przedmiot i zapisuję do grupy. Próba wypisania studenta powinna zakończyć się błędem z powodu naruszenia więzów integralności.
declare
   v_s_id   number := 201;
   v_c_code varchar2(5) := 'PZSP1';
   v_s_code varchar2(5) := '24Z';
   v_g_num  number := 101;
begin
   insert into final_grades values ( v_s_id,
                                     v_c_code,
                                     v_s_code,
                                     null );
   insert into students_in_groups values ( v_s_id,
                                           v_c_code,
                                           v_s_code,
                                           v_g_num );
   delete from final_grades
    where user_id = v_s_id
      and course_code = v_c_code
      and semester = v_s_code;
   dbms_output.put_line('Test niezdany');
   rollback;
exception
   when others then
      dbms_output.put_line('TEST ZDANY! -> ' || sqlerrm);
      rollback;
end;
/
--Zapisuję studenta na przedmiot, student dostaje ocene czastkową. Próba wypisania studenta powinna zakończyć się błędem z powodu naruszenia więzów integralności.
declare
   v_s_id   number := 201;
   v_c_code varchar2(5) := 'PZSP1';
   v_s_code varchar2(5) := '24Z';
   v_cat_id number := 61;
begin
   insert into final_grades values ( v_s_id,
                                     v_c_code,
                                     v_s_code,
                                     null );
   insert into grades values ( v_cat_id,
                               v_c_code,
                               v_s_code,
                               v_s_id,
                               80,
                               84,
                               default,
                               null );
   delete from final_grades
    where user_id = v_s_id
      and course_code = v_c_code
      and semester = v_s_code;
   dbms_output.put_line('Test niezdany');
   rollback;
exception
   when others then
      dbms_output.put_line('TEST ZDANY! -> ' || sqlerrm);
      rollback;
end;
/
--Wstawienie oceny końcowej z poza przedziału [2,5]
declare
   v_s_id   number := 201;
   v_c_code varchar2(5) := 'PZSP1';
   v_s_code varchar2(5) := '24Z';
   v_cat_id number := 61;
begin
   insert into final_grades values ( v_s_id,
                                     v_c_code,
                                     v_s_code,
                                     6 );
   dbms_output.put_line('Test niezdany');
   rollback;
exception
   when others then
      dbms_output.put_line('TEST ZDANY! -> ' || sqlerrm);
      rollback;
end;
/
--Wstawienie oceny końcowej z przedziału [2,5] ale nie podzielnej przez 0.5
declare
   v_s_id   number := 201;
   v_c_code varchar2(5) := 'PZSP1';
   v_s_code varchar2(5) := '24Z';
   v_cat_id number := 61;
begin
   insert into final_grades values ( v_s_id,
                                     v_c_code,
                                     v_s_code,
                                     4.25 );
   dbms_output.put_line('Test niezdany');
   rollback;
exception
   when others then
      dbms_output.put_line('TEST ZDANY! -> ' || sqlerrm);
      rollback;
end;
/
--Usunięcie przedmiotu z semestru jesli istnieje grupa zajęciowa. Operacja powinna zakończyć sie niepowodzeniem
declare
   v_s_id   number := 201;
   v_c_code varchar2(5) := 'PZSP1';
   v_s_code varchar2(5) := '24Z';
   v_cat_id number := 61;
begin
   delete from courses_in_semester
    where course_code = v_c_code
      and semester = v_s_code;
   dbms_output.put_line('Test niezdany');
   rollback;
exception
   when others then
      dbms_output.put_line('TEST ZDANY! -> ' || sqlerrm);
      rollback;
end;
/