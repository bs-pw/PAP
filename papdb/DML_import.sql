insert into attendance_statuses (
   attendance_status_id,
   status
) values ( 1,
           'Present' );

insert into class_types (
   class_type_id,
   type
) values ( 2,
           'Tutorial' );
insert into class_types (
   class_type_id,
   type
) values ( 1,
           'Lecture' );
insert into class_types (
   class_type_id,
   type
) values ( 3,
           'Laboratory' );
insert into class_types (
   class_type_id,
   type
) values ( 4,
           'Project' );

insert into semesters (
   semester_code,
   start_date,
   end_date
) values ( '24Z',
           date '2024-10-02',
           date '2025-02-16' );
insert into semesters (
   semester_code,
   start_date,
   end_date
) values ( '30L',
           date '2030-02-01',
           date '2030-06-20' );
insert into semesters (
   semester_code,
   start_date,
   end_date
) values ( '25L',
           date '2025-01-01',
           date '2025-12-31' );

insert into courses (
   course_code,
   title
) values ( 'PAP',
           'Programowanie Aplikacyjne' );
insert into courses (
   course_code,
   title
) values ( 'SOI',
           'Systemy Operacyjne' );
insert into courses (
   course_code,
   title
) values ( 'BD1',
           'Bazy danych 1' );
insert into courses (
   course_code,
   title
) values ( 'PZSP1',
           'Projekt zespołowy 1' );
insert into courses (
   course_code,
   title
) values ( 'PROB',
           'Probabilistyka' );
insert into courses (
   course_code,
   title
) values ( 'MAKO1',
           'Matematyka konkretna 1' );

insert into courses_in_semester (
   course_code,
   semester,
   is_closed
) values ( 'SOI',
           '24Z',
           0 );
insert into courses_in_semester (
   course_code,
   semester,
   is_closed
) values ( 'PZSP1',
           '24Z',
           0 );
insert into courses_in_semester (
   course_code,
   semester,
   is_closed
) values ( 'BD1',
           '24Z',
           1 );
insert into courses_in_semester (
   course_code,
   semester,
   is_closed
) values ( 'PAP',
           '30L',
           0 );
insert into courses_in_semester (
   course_code,
   semester,
   is_closed
) values ( 'PROB',
           '24Z',
           0 );
insert into courses_in_semester (
   course_code,
   semester,
   is_closed
) values ( 'PAP',
           '24Z',
           1 );
insert into courses_in_semester (
   course_code,
   semester,
   is_closed
) values ( 'MAKO1',
           '24Z',
           0 );

insert into user_types (
   user_type_id,
   type
) values ( 0,
           'admin' );
insert into user_types (
   user_type_id,
   type
) values ( 1,
           'lecturer' );
insert into user_types (
   user_type_id,
   type
) values ( 2,
           'assistant' );
insert into user_types (
   user_type_id,
   type
) values ( 3,
           'student' );
insert into user_types (
   user_type_id,
   type
) values ( 4,
           'inactive' );

insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 1,
           'Sylwia',
           'Lipska',
           '{bcrypt}$2a$10$ROIxtRWZPqTi41ieqKkffepG8U5w2eAAT1FJgta2aPHB40LGynpqK',
           'sylwia.lipska@pw.edu.pl',
           0 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 2,
           'Patryk',
           'Skręta',
           '{bcrypt}$2y$10$m07/RO9X1kWE8zc1EhVguOED.H7I3WOb2B0cuwyUedGYARXQ.bFGi',
           'pskreta@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 3,
           'Piotr',
           'Maciąg',
           '{bcrypt}$2a$10$py85cg0hHt3pHkNHpT4keOydKnPDaqqgIMTgWKAKDRQVuPj32zNmi',
           'pmaciag@pw.edu.pl',
           1 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 21,
           'Test',
           'Testowy',
           '{bcrypt}$2a$10$dUlWRd7TQUoktQYt7haSg.qH3oJjGXbLjs.RrlzsdB.Za2LimPExe',
           'test@test.pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 121,
           'Wojciech',
           'Sitek',
           '{bcrypt}$2a$10$yNw5lCk3WsRJEEiAAgyj0.FrpWr0N2FxIbTrI26ut7e2jLb8WkvU.',
           'ws@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 101,
           'Jan',
           'Kowalski',
           '{bcrypt}$2a$10$/T/T1FZF8Y5mQjtTPbWDjOC2rnFHQJI8nxiP/B61beemmftL2KWGm',
           'jkowal@pw.edu.pl',
           1 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 102,
           'Janina',
           'Kowalska',
           '{bcrypt}$2a$10$9zIRAkMCmyUtTzjVS94rbegVitur/qytXIJ280NyIqLbFymYx9MwK',
           'jkowal1@pw.edu.pl',
           1 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 64,
           'Maks',
           'Domagała',
           '{bcrypt}$2a$10$rCmZojE3UApVFrZjOqfMUeHFHf2kQf5JhGP96oAfi1.H7ZMQuVrf6',
           'mdomagal@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 141,
           'Name_BCCXN',
           'Surname_YWCDS',
           '296K01ES',
           'student_1@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 83,
           'Maciej',
           'Borkowski',
           '{bcrypt}$2a$10$dZ2ioQjtz6naPWNuMa2xFujIXO8aZmYhA14DBje6oY7.fRHq.k2mu',
           'mborkow5@pw.edu.pl',
           2 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 84,
           'Andrzej',
           'Zalewski',
           '{bcrypt}$2a$10$rutGPjl4aeMOUlUX/Lxq2uIi1wt7T0nlApgjEOUEkHsuhDI6xmPaG',
           'azalewski@pw.edu.pl',
           1 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 142,
           'Name_UXYBW',
           'Surname_XZACE',
           'AHGC1TAK',
           'student_2@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 143,
           'Name_SWKBY',
           'Surname_LQDHJ',
           'CSO5BZZT',
           'student_3@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 144,
           'Name_SACEC',
           'Surname_UKILE',
           'ICUWFE4C',
           'student_4@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 145,
           'Name_FLAFL',
           'Surname_ESQDK',
           '6AB5C40D',
           'student_5@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 146,
           'Name_KDEVG',
           'Surname_MUMAS',
           'RG4WLC7F',
           'student_6@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 147,
           'Name_AKRGP',
           'Surname_HCTMU',
           'BJP81L9D',
           'student_7@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 148,
           'Name_VAYBX',
           'Surname_EQVOT',
           'RDPBJ0DL',
           'student_8@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 149,
           'Name_QMWRQ',
           'Surname_OAMSB',
           'A0UYWIE4',
           'student_9@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 150,
           'Name_PRFFZ',
           'Surname_IRUBA',
           'SP5DPW9W',
           'student_10@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 151,
           'Name_YVNQC',
           'Surname_FFMUM',
           '85IBZR7D',
           'student_11@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 152,
           'Name_YIHYS',
           'Surname_LGEWL',
           'YKWSTF5I',
           'student_12@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 153,
           'Name_IVEAR',
           'Surname_FNECB',
           'S023OZTY',
           'student_13@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 154,
           'Name_IFZJB',
           'Surname_BUCHQ',
           'VEOU45TW',
           'student_14@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 155,
           'Name_PLMJZ',
           'Surname_MLIGO',
           'ESI80TS8',
           'student_15@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 156,
           'Name_PDXLR',
           'Surname_OQEAF',
           '7MWNJ5CT',
           'student_16@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 157,
           'Name_LUNGY',
           'Surname_MWHJH',
           'FOM9Y1LG',
           'student_17@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 158,
           'Name_MFQMP',
           'Surname_FCJZU',
           'PRLZ1J2P',
           'student_18@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 159,
           'Name_SLLGT',
           'Surname_CGGLZ',
           '9T2DV6NY',
           'student_19@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 160,
           'Name_AOENB',
           'Surname_QGLHK',
           'TIEBPEBC',
           'student_20@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 161,
           'Name_OSLCM',
           'Surname_WAETV',
           'ZCPMJ713',
           'student_21@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 162,
           'Name_IYXSR',
           'Surname_OPQOD',
           'DK5M2B93',
           'student_22@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 163,
           'Name_VBTXJ',
           'Surname_AZYBV',
           'LVLQ8L3Y',
           'student_23@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 164,
           'Name_XEIAF',
           'Surname_VLFVV',
           '58GJOA4E',
           'student_24@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 165,
           'Name_DIWYO',
           'Surname_HKFJU',
           'QHJ1SFCH',
           'student_25@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 166,
           'Name_YHGOT',
           'Surname_NGLNT',
           '3Q9Q24JN',
           'student_26@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 167,
           'Name_GIJSV',
           'Surname_ZIZGP',
           '9HPETXUV',
           'student_27@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 168,
           'Name_XJTKS',
           'Surname_QARYL',
           'XA4SD555',
           'student_28@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 169,
           'Name_LGGME',
           'Surname_ZAZVI',
           'NYM64TI8',
           'student_29@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 170,
           'Name_HGZHE',
           'Surname_TGLUP',
           'REN6N3E0',
           'student_30@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 171,
           'Name_WXJPS',
           'Surname_JPQWB',
           '9GIF1DGA',
           'student_31@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 172,
           'Name_XSSBC',
           'Surname_XKQDM',
           'E2YH3HE8',
           'student_32@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 173,
           'Name_PXXWJ',
           'Surname_KFUWV',
           'Z7PJVD5I',
           'student_33@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 174,
           'Name_YGONB',
           'Surname_DLODY',
           'JNO4BX8S',
           'student_34@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 175,
           'Name_SCMPW',
           'Surname_CDCFA',
           'UKLWRNZG',
           'student_35@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 176,
           'Name_JAMAU',
           'Surname_XBWXF',
           'R70ENCY0',
           'student_36@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 177,
           'Name_PBJTN',
           'Surname_YRALW',
           'LJ405SY1',
           'student_37@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 178,
           'Name_SUZJU',
           'Surname_QHDMW',
           'CTK6X3EI',
           'student_38@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 179,
           'Name_KABYW',
           'Surname_KLEXB',
           'ZP8P6MPQ',
           'student_39@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 180,
           'Name_QXEVQ',
           'Surname_WTGFK',
           'Y01J17PT',
           'student_40@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 181,
           'Name_LDYWL',
           'Surname_UINVR',
           'H2J833X1',
           'student_41@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 182,
           'Name_KEHGI',
           'Surname_WKMSY',
           'APJOT4I5',
           'student_42@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 183,
           'Name_NZLZN',
           'Surname_DYHMH',
           'IW1FOBH1',
           'student_43@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 184,
           'Name_RVPRV',
           'Surname_SMWFQ',
           'T0727I3G',
           'student_44@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 185,
           'Name_EILTO',
           'Surname_VTLXK',
           'ARR7IESB',
           'student_45@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 186,
           'Name_WAWQQ',
           'Surname_IDDJR',
           'SLD7R531',
           'student_46@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 187,
           'Name_WLHNB',
           'Surname_CMGCS',
           '1LB3224P',
           'student_47@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 188,
           'Name_UHMVL',
           'Surname_JLDRR',
           'JDSMNSWV',
           'student_48@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 189,
           'Name_KJGYI',
           'Surname_RYLFL',
           'RXA4H8ZJ',
           'student_49@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 190,
           'Name_KHOYE',
           'Surname_PMHXT',
           'IYHRGB1G',
           'student_50@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 191,
           'Name_JSTSH',
           'Surname_FHSGD',
           'XIW6X8Q3',
           'student_51@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 192,
           'Name_MXQDS',
           'Surname_VPKFR',
           'RSEOU8Y9',
           'student_52@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 193,
           'Name_TRAVC',
           'Surname_DDJLV',
           'Z82YN2GQ',
           'student_53@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 194,
           'Name_HZCYJ',
           'Surname_BASKC',
           'DVROVZ2U',
           'student_54@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 195,
           'Name_FPGCZ',
           'Surname_RBRJV',
           'R8JPNY6C',
           'student_55@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 196,
           'Name_HUKLY',
           'Surname_MAXBI',
           'S12AJW6F',
           'student_56@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 197,
           'Name_KJQMU',
           'Surname_DGOKA',
           '1J72G9EZ',
           'student_57@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 198,
           'Name_HGMIL',
           'Surname_TSDYX',
           'RXNX2AFY',
           'student_58@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 199,
           'Name_MOIUM',
           'Surname_ZAFYN',
           'L70DI18L',
           'student_59@pw.edu.pl',
           3 );
insert into users (
   user_id,
   name,
   surname,
   password,
   mail,
   user_type_id
) values ( 200,
           'Name_ECNPK',
           'Surname_OCXMM',
           'BHHOMT1V',
           'student_60@pw.edu.pl',
           3 );

insert into coordinators (
   user_id,
   course_code,
   semester
) values ( 3,
           'PROB',
           '24Z' );
insert into coordinators (
   user_id,
   course_code,
   semester
) values ( 84,
           'BD1',
           '24Z' );
insert into coordinators (
   user_id,
   course_code,
   semester
) values ( 84,
           'SOI',
           '24Z' );
insert into coordinators (
   user_id,
   course_code,
   semester
) values ( 101,
           'BD1',
           '24Z' );
insert into coordinators (
   user_id,
   course_code,
   semester
) values ( 101,
           'PROB',
           '24Z' );
insert into coordinators (
   user_id,
   course_code,
   semester
) values ( 101,
           'SOI',
           '24Z' );

insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 64,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 83,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 2,
           'BD1',
           '24Z',
           5.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 2,
           'SOI',
           '24Z',
           5.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 21,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 21,
           'PROB',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 2,
           'PROB',
           '24Z',
           null );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 2,
           'PAP',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 121,
           'BD1',
           '24Z',
           3.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 83,
           'BD1',
           '24Z',
           3.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 142,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 143,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 144,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 145,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 146,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 147,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 148,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 149,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 150,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 151,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 152,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 153,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 154,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 155,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 156,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 157,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 158,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 159,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 160,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 161,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 162,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 163,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 164,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 165,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 166,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 167,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 168,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 169,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 170,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 171,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 172,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 173,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 174,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 175,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 176,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 177,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 178,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 179,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 180,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 181,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 182,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 183,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 184,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 185,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 186,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 187,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 188,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 189,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 190,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 191,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 192,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 193,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 194,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 195,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 196,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 197,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 198,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 199,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 200,
           'SOI',
           '24Z',
           2.0 );
insert into final_grades (
   user_id,
   course_code,
   semester,
   grade
) values ( 141,
           'BD1',
           '24Z',
           2.0 );

insert into grade_categories (
   category_id,
   course_code,
   semester,
   description,
   max_grade
) values ( 1,
           'SOI',
           '24Z',
           'Kolokwium',
           20.00 );
insert into grade_categories (
   category_id,
   course_code,
   semester,
   description,
   max_grade
) values ( 2,
           'SOI',
           '24Z',
           'Egzamin',
           40.00 );
insert into grade_categories (
   category_id,
   course_code,
   semester,
   description,
   max_grade
) values ( 3,
           'SOI',
           '24Z',
           'Laboratorium I',
           8.00 );
insert into grade_categories (
   category_id,
   course_code,
   semester,
   description,
   max_grade
) values ( 4,
           'SOI',
           '24Z',
           'Laboratorium II',
           8.00 );
insert into grade_categories (
   category_id,
   course_code,
   semester,
   description,
   max_grade
) values ( 21,
           'SOI',
           '24Z',
           'Laboratorium III',
           8.00 );
insert into grade_categories (
   category_id,
   course_code,
   semester,
   description,
   max_grade
) values ( 22,
           'SOI',
           '24Z',
           'Laboratorium IV',
           4.00 );
insert into grade_categories (
   category_id,
   course_code,
   semester,
   description,
   max_grade
) values ( 23,
           'SOI',
           '24Z',
           'Laboratorium V',
           4.00 );
insert into grade_categories (
   category_id,
   course_code,
   semester,
   description,
   max_grade
) values ( 24,
           'SOI',
           '24Z',
           'Laboratorium VI',
           8.00 );
insert into grade_categories (
   category_id,
   course_code,
   semester,
   description,
   max_grade
) values ( 25,
           'PROB',
           '24Z',
           'Kolokwium 1',
           16.00 );
insert into grade_categories (
   category_id,
   course_code,
   semester,
   description,
   max_grade
) values ( 27,
           'PROB',
           '24Z',
           'Egzamin',
           60.00 );
insert into grade_categories (
   category_id,
   course_code,
   semester,
   description,
   max_grade
) values ( 28,
           'PROB',
           '24Z',
           'Aktywność',
           8.00 );
insert into grade_categories (
   category_id,
   course_code,
   semester,
   description,
   max_grade
) values ( 29,
           'BD1',
           '24Z',
           'Kolokwium',
           20.00 );
insert into grade_categories (
   category_id,
   course_code,
   semester,
   description,
   max_grade
) values ( 30,
           'BD1',
           '24Z',
           'Egzamin',
           15.00 );

insert into grades (
   category_id,
   course_code,
   semester,
   user_id,
   who_inserted_id,
   grade,
   "date",
   description
) values ( 1,
           'SOI',
           '24Z',
           2,
           84,
           19.00,
           timestamp '2025-01-22 23:10:37.000000',
           'W porządku. Powinno być bez kluczy obcych.' );
insert into grades (
   category_id,
   course_code,
   semester,
   user_id,
   who_inserted_id,
   grade,
   "date",
   description
) values ( 2,
           'SOI',
           '24Z',
           2,
           84,
           35.00,
           timestamp '2025-01-22 23:10:37.000000',
           'super' );
insert into grades (
   category_id,
   course_code,
   semester,
   user_id,
   who_inserted_id,
   grade,
   "date",
   description
) values ( 3,
           'SOI',
           '24Z',
           83,
           3,
           7.99,
           timestamp '2025-01-17 01:34:52.000000',
           'AAA' );
insert into grades (
   category_id,
   course_code,
   semester,
   user_id,
   who_inserted_id,
   grade,
   "date",
   description
) values ( 1,
           'SOI',
           '24Z',
           83,
           84,
           20.00,
           timestamp '2025-01-17 19:34:52.000000',
           'AAA' );
insert into grades (
   category_id,
   course_code,
   semester,
   user_id,
   who_inserted_id,
   grade,
   "date",
   description
) values ( 4,
           'SOI',
           '24Z',
           83,
           3,
           4.00,
           timestamp '2025-01-17 01:34:52.000000',
           'AAA' );
insert into grades (
   category_id,
   course_code,
   semester,
   user_id,
   who_inserted_id,
   grade,
   "date",
   description
) values ( 4,
           'SOI',
           '24Z',
           2,
           84,
           8.00,
           timestamp '2025-01-22 23:10:38.000000',
           'Słabo' );
insert into grades (
   category_id,
   course_code,
   semester,
   user_id,
   who_inserted_id,
   grade,
   "date",
   description
) values ( 29,
           'BD1',
           '24Z',
           141,
           84,
           12.00,
           timestamp '2025-01-22 21:41:43.000000',
           null );
insert into grades (
   category_id,
   course_code,
   semester,
   user_id,
   who_inserted_id,
   grade,
   "date",
   description
) values ( 2,
           'SOI',
           '24Z',
           83,
           3,
           2.00,
           timestamp '2025-01-17 01:35:22.000000',
           ':)' );
insert into grades (
   category_id,
   course_code,
   semester,
   user_id,
   who_inserted_id,
   grade,
   "date",
   description
) values ( 25,
           'PROB',
           '24Z',
           21,
           102,
           12.00,
           timestamp '2025-01-17 19:12:08.000000',
           'Błąd w zadaniu 3.' );
insert into grades (
   category_id,
   course_code,
   semester,
   user_id,
   who_inserted_id,
   grade,
   "date",
   description
) values ( 28,
           'PROB',
           '24Z',
           21,
           102,
           4.00,
           timestamp '2025-01-17 19:12:08.000000',
           'Zadanie przy tablicy' );
insert into grades (
   category_id,
   course_code,
   semester,
   user_id,
   who_inserted_id,
   grade,
   "date",
   description
) values ( 1,
           'SOI',
           '24Z',
           64,
           84,
           5.00,
           timestamp '2025-01-17 19:34:52.000000',
           'kom' );
insert into grades (
   category_id,
   course_code,
   semester,
   user_id,
   who_inserted_id,
   grade,
   "date",
   description
) values ( 3,
           'SOI',
           '24Z',
           2,
           84,
           8.00,
           timestamp '2025-01-22 23:10:38.000000',
           null );
insert into grades (
   category_id,
   course_code,
   semester,
   user_id,
   who_inserted_id,
   grade,
   "date",
   description
) values ( 21,
           'SOI',
           '24Z',
           2,
           84,
           8.00,
           timestamp '2025-01-22 23:10:38.000000',
           null );
insert into grades (
   category_id,
   course_code,
   semester,
   user_id,
   who_inserted_id,
   grade,
   "date",
   description
) values ( 22,
           'SOI',
           '24Z',
           2,
           84,
           4.00,
           timestamp '2025-01-22 23:10:38.000000',
           null );
insert into grades (
   category_id,
   course_code,
   semester,
   user_id,
   who_inserted_id,
   grade,
   "date",
   description
) values ( 23,
           'SOI',
           '24Z',
           2,
           84,
           4.00,
           timestamp '2025-01-22 23:10:38.000000',
           null );
insert into grades (
   category_id,
   course_code,
   semester,
   user_id,
   who_inserted_id,
   grade,
   "date",
   description
) values ( 24,
           'SOI',
           '24Z',
           2,
           84,
           8.00,
           timestamp '2025-01-22 23:10:38.000000',
           null );

insert into groups (
   course_code,
   group_number,
   semester
) values ( 'BD1',
           104,
           '24Z' );
insert into groups (
   course_code,
   group_number,
   semester
) values ( 'BD1',
           105,
           '24Z' );
insert into groups (
   course_code,
   group_number,
   semester
) values ( 'PROB',
           101,
           '24Z' );
insert into groups (
   course_code,
   group_number,
   semester
) values ( 'SOI',
           1,
           '24Z' );
insert into groups (
   course_code,
   group_number,
   semester
) values ( 'SOI',
           33,
           '24Z' );
insert into groups (
   course_code,
   group_number,
   semester
) values ( 'SOI',
           104,
           '24Z' );
insert into groups (
   course_code,
   group_number,
   semester
) values ( 'SOI',
           201,
           '24Z' );

insert into students_in_groups (
   user_id,
   course_code,
   semester,
   group_number
) values ( 2,
           'BD1',
           '24Z',
           104 );
insert into students_in_groups (
   user_id,
   course_code,
   semester,
   group_number
) values ( 2,
           'SOI',
           '24Z',
           1 );
insert into students_in_groups (
   user_id,
   course_code,
   semester,
   group_number
) values ( 64,
           'SOI',
           '24Z',
           1 );
insert into students_in_groups (
   user_id,
   course_code,
   semester,
   group_number
) values ( 64,
           'SOI',
           '24Z',
           104 );
insert into students_in_groups (
   user_id,
   course_code,
   semester,
   group_number
) values ( 83,
           'SOI',
           '24Z',
           104 );
insert into students_in_groups (
   user_id,
   course_code,
   semester,
   group_number
) values ( 121,
           'BD1',
           '24Z',
           104 );

insert into lecturers (
   user_id,
   course_code,
   semester,
   group_number
) values ( 3,
           'SOI',
           '24Z',
           104 );
insert into lecturers (
   user_id,
   course_code,
   semester,
   group_number
) values ( 84,
           'BD1',
           '24Z',
           104 );
insert into lecturers (
   user_id,
   course_code,
   semester,
   group_number
) values ( 102,
           'PROB',
           '24Z',
           101 );

insert into classes (
   course_code,
   group_number,
   semester,
   class_id_for_group,
   day,
   hour,
   length,
   "where",
   class_type_id
) values ( 'SOI',
           1,
           '24Z',
           28,
           1,
           735,
           90,
           '133',
           1 );
insert into classes (
   course_code,
   group_number,
   semester,
   class_id_for_group,
   day,
   hour,
   length,
   "where",
   class_type_id
) values ( 'SOI',
           104,
           '24Z',
           21,
           0,
           735,
           92,
           '09',
           3 );
insert into classes (
   course_code,
   group_number,
   semester,
   class_id_for_group,
   day,
   hour,
   length,
   "where",
   class_type_id
) values ( 'SOI',
           104,
           '24Z',
           42,
           3,
           500,
           90,
           '105',
           3 );
insert into classes (
   course_code,
   group_number,
   semester,
   class_id_for_group,
   day,
   hour,
   length,
   "where",
   class_type_id
) values ( 'SOI',
           104,
           '24Z',
           26,
           3,
           4,
           5,
           '6',
           2 );
insert into classes (
   course_code,
   group_number,
   semester,
   class_id_for_group,
   day,
   hour,
   length,
   "where",
   class_type_id
) values ( 'BD1',
           104,
           '24Z',
           27,
           5,
           1000,
           90,
           '411',
           2 );
insert into classes (
   course_code,
   group_number,
   semester,
   class_id_for_group,
   day,
   hour,
   length,
   "where",
   class_type_id
) values ( 'BD1',
           104,
           '24Z',
           29,
           1,
           800,
           90,
           '111',
           4 );
insert into classes (
   course_code,
   group_number,
   semester,
   class_id_for_group,
   day,
   hour,
   length,
   "where",
   class_type_id
) values ( 'PROB',
           101,
           '24Z',
           41,
           4,
           795,
           60,
           '107',
           2 );