package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.Group;
import pap.z27.papapi.domain.User;
import pap.z27.papapi.domain.subclasses.*;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepo {
    @Autowired
    private final JdbcClient jdbcClient;
    public UserRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Integer countUserById(Integer userId) {
        return jdbcClient.sql("SELECT COUNT(*) FROM USERS where user_id=?")
                .param(userId)
                .query(Integer.class)
                .single();
    }
    public List<UserPublicInfo> findAllUsers() {
        return jdbcClient.sql("SELECT u.user_id,u.name,u.surname,ut.type,u.mail" +
                        " FROM USERS u join USER_TYPES ut using(USER_TYPE_ID)")
                .query(UserPublicInfo.class)
                .list();
    }
    public String findPasswordByMail(String mail) {
        return jdbcClient.sql("SELECT password FROM USERS where mail=?")
                .param(mail)
                .query(Password.class)
                .single()
                .getPassword();
    }
    public UserInfo findUsersInfoByID(Integer userId) {
        return jdbcClient.sql("SELECT u.user_id,u.name,u.surname,USER_TYPE_ID,ut.type,u.mail" +
                        " FROM USERS u join USER_TYPES ut using(USER_TYPE_ID) where user_id=?")
                .param(userId)
                .query(UserInfo.class)
                .single();
    }
    public UserPublicInfo findUsersInfoByMail(String mail) {
        return jdbcClient.sql("SELECT u.user_id,u.name,u.surname,ut.type,u.mail" +
                        " FROM USERS u join USER_TYPES ut using(USER_TYPE_ID) where mail=?")
                .param(mail)
                .query(UserPublicInfo.class)
                .single();
    }
    public UserLoginInfo findUsersLoginInfoByMail(String mail) {
        return jdbcClient.sql("SELECT u.user_id,u.name,u.surname,u.user_type_id,u.mail" +
                        " FROM USERS u where mail=?")
                .param(mail)
                .query(UserLoginInfo.class)
                .single();
    }
    public List<UserPublicInfo> findAllCourseCoordinators(CourseInSemester courseInSemester) {
        return jdbcClient.sql("SELECT u.user_id,u.name,u.surname,ut.type,u.mail" +
                        " FROM USERS u join COORDINATORS c ON u.user_id = c.user_id join USER_TYPES ut using(USER_TYPE_ID)" +
                        " where c.course_code = ? and c.semester = ?")
                .param(courseInSemester.getCourse_code())
                .param(courseInSemester.getSemester())
                .query(UserPublicInfo.class)
                .list();
    }
    public List<UserPublicInfo> findAllEligibleCourseCoordinators(CourseInSemester courseInSemester) {
        return jdbcClient.sql("Select users.user_id, users.name, users.surname, user_types.type, users.mail " +
                        "from users " +
                        "         join user_types on users.user_type_id = user_types.user_type_id " +
                        "where USER_TYPES.USER_TYPE_ID in (1,2) " +
                        "MINUS " +
                        "Select users.user_id, users.name, users.surname, user_types.type, users.mail " +
                        "from users " +
                        "         join final_grades on final_grades.user_id=users.user_id " +
                        "         join user_types on users.user_type_id = user_types.user_type_id " +
                        "where FINAL_GRADES.SEMESTER=? and FINAL_GRADES.COURSE_CODE=? " +
                        "MINUS " +
                        "Select users.user_id, users.name, users.surname, user_types.type, users.mail " +
                        "from users " +
                        "         join COORDINATORS on COORDINATORS.user_id=users.user_id " +
                        "         join user_types on users.user_type_id = user_types.user_type_id " +
                        "where COORDINATORS.SEMESTER = ? and COORDINATORS.COURSE_CODE=?")
                .param(courseInSemester.getSemester())
                .param(courseInSemester.getCourse_code())
                .param(courseInSemester.getSemester())
                .param(courseInSemester.getCourse_code())
                .query(UserPublicInfo.class)
                .list();
    }
    public List<UserPublicInfo> findAllEligibleUsersToCourse(CourseInSemester courseInSemester) {
        return jdbcClient.sql("Select users.user_id, users.name, users.surname, user_types.type, users.mail " +
                        "from users " +
                        "         join user_types on users.user_type_id = user_types.user_type_id " +
                        "where USER_TYPES.USER_TYPE_ID in (2,3) " +
                        "MINUS " +
                        "Select users.user_id, users.name, users.surname, user_types.type, users.mail " +
                        "from users " +
                        "         join final_grades on final_grades.user_id=users.user_id " +
                        "         join user_types on users.user_type_id = user_types.user_type_id " +
                        "where FINAL_GRADES.SEMESTER=? and FINAL_GRADES.COURSE_CODE=? " +
                        "MINUS " +
                        "Select users.user_id, users.name, users.surname, user_types.type, users.mail " +
                        "from users " +
                        "         join COORDINATORS on COORDINATORS.user_id=users.user_id " +
                        "         join user_types on users.user_type_id = user_types.user_type_id " +
                        "where COORDINATORS.SEMESTER = ? and COORDINATORS.COURSE_CODE=?" +
                        "MINUS " +
                        "Select users.user_id, users.name, users.surname, user_types.type, users.mail " +
                        "from users " +
                        "         join LECTURERS on LECTURERS.user_id=users.user_id " +
                        "         join user_types on users.user_type_id = user_types.user_type_id " +
                        "where LECTURERS.SEMESTER = ? and LECTURERS.COURSE_CODE=?")
                .param(courseInSemester.getSemester())
                .param(courseInSemester.getCourse_code())
                .param(courseInSemester.getSemester())
                .param(courseInSemester.getCourse_code())
                .param(courseInSemester.getSemester())
                .param(courseInSemester.getCourse_code())
                .query(UserPublicInfo.class)
                .list();
    }
    public List<UserPublicInfo> findAllUsersInGroup(Group group) {
        return jdbcClient.sql("SELECT user_id,u.name,u.surname,ut.type,u.mail" +
                        " FROM USERS u join STUDENTS_IN_GROUPS sin using(user_id) join USER_TYPES ut using(USER_TYPE_ID)" +
                        " WHERE sin.course_code = ? AND sin.semester = ? AND sin.group_number = ?")
                .param(group.getCourse_code())
                .param(group.getSemester())
                .param(group.getGroup_number())
                .query(UserPublicInfo.class)
                .list();
    }
    public List<UserPublicInfo> findAllLecturersInGroup(Group group) {
        return jdbcClient.sql("SELECT u.user_id,u.name,u.surname,ut.type,u.mail" +
                        " FROM USERS u join LECTURERS l on(l.user_id=u.user_id) join USER_TYPES ut using(USER_TYPE_ID)" +
                        " where l.course_code = ? and l.semester = ? and l.group_number=?")
                .param(group.getCourse_code())
                .param(group.getSemester())
                .param(group.getGroup_number())
                .query(UserPublicInfo.class)
                .list();
    }
    public Integer insertUser(User user) {
        return jdbcClient.sql("INSERT INTO USERS (name, surname, password, mail, user_type_id) VALUES (?,?,?,?,?)")
                .param(user.getName())
                .param(user.getSurname())
                .param(user.getPassword())
                .param(user.getMail())
                .param(user.getUser_type_id())
                .update();
    }
    public Integer updateUser(Integer userId, User user) {
        StringBuilder query = new StringBuilder("UPDATE USERS set ");
        List<Object> params=new ArrayList<>(){};
        if(user.getName()!=null)
        {
            query.append("name=?, ");
            params.add(user.getName());
        }
        if(user.getSurname()!=null)
        {
            query.append("surname=?, ");
            params.add(user.getSurname());
        }
        if(user.getPassword()!=null)
        {
            query.append("password=?, ");
            params.add(user.getPassword());
        }
        if(user.getMail()!=null)
        {
            query.append("mail=?, ");
            params.add(user.getMail());
        }
        if(user.getUser_type_id()!=null)
        {
            query.append("user_type_id=?, ");
            params.add(user.getUser_type_id());
        }
        if(params.isEmpty()) return 0;
        else {
            query.setLength(query.length() - 2);
            query.append(" WHERE user_id=?");
            params.add(userId);
            return jdbcClient.sql(query.toString())
                    .params(params)
                    .update();
        }
    }

    public Integer updateUsersPasswordOrMail(Integer userId, User user) {
        StringBuilder query = new StringBuilder("UPDATE USERS set ");
        List<Object> params=new ArrayList<>(){};
        if(user.getPassword()!=null)
        {
            query.append("password=?, ");
            params.add(user.getPassword());
        }
        if(user.getMail()!=null)
        {
            query.append("mail=?, ");
            params.add(user.getMail());
        }
        if(params.isEmpty()) return 0;
        else {
            query.setLength(query.length() - 2);
            query.append(" WHERE user_id=?");
            params.add(userId);
            return jdbcClient.sql(query.toString())
                    .params(params)
                    .update();
        }
    }
    public Integer updateUsersType(Integer userId, Integer type) {
        return jdbcClient.sql("UPDATE USERS set USER_TYPE_ID=? where user_id=?")
                .param(type)
                .param(userId)
                .update();
    }

    public Integer findUsersTypeId(Integer userId) {
        return jdbcClient.sql("SELECT USER_TYPE_ID from USERS where USER_ID=?")
                .param(userId)
                .query(Integer.class)
                .single();
    }
//    public Type findUsersType(Integer userId) {
//        return jdbcClient.sql("SELECT USER_TYPE_ID from USERS where USER_ID=?")
//                .param(userId)
//                .query(Type.class)
//                .single();
//    }
    public Integer countUsersFinalGrades(UserInGroup userInGroup){
        return jdbcClient.sql("SELECT count(*) FROM FINAL_GRADES where user_id=? and COURSE_CODE=? and SEMESTER=?")
                .param(userInGroup.getUser_id())
                .param(userInGroup.getCourse_code())
                .param(userInGroup.getSemester())
                .query(Integer.class)
                .single();
    }
//    public String getUserStatus(Integer userId) {
//        return jdbcClient.sql("SELECT status from USERS where USER_ID=?")
//                .param(userId)
//                .query(Status.class);
//    }


//    public List<Attendance> findAllAttendances() {
//        return jdbcClient.sql("SELECT * FROM USERS").query(User.class).list();
//    }

    public Integer checkIfIsCoordinator(UserInGroup userInGroup){
        return jdbcClient.sql("SELECT count(*) FROM COORDINATORS where user_id=? and COURSE_CODE=? and SEMESTER=?")
                .param(userInGroup.getUser_id())
                .param(userInGroup.getCourse_code())
                .param(userInGroup.getSemester())
                .query(Integer.class)
                .single();
    }

    public Integer checkIfIsCoordinator(Integer userId, String courseCode, String semester) {
        return jdbcClient.sql("SELECT count(*) FROM COORDINATORS where user_id=? and COURSE_CODE=? and SEMESTER=?")
                .param(userId)
                .param(courseCode)
                .param(semester)
                .query(Integer.class)
                .single();
    }
    public Integer checkIfIsLecturer(Integer userId, String courseCode, String semester, Integer group_number) {
        return jdbcClient.sql("SELECT count(*) FROM LECTURERS where user_id=? and COURSE_CODE=? and SEMESTER=? and GROUP_NUMBER=?")
                .param(userId)
                .param(courseCode)
                .param(semester)
                .param(group_number)
                .query(Integer.class)
                .single();
    }
    public Integer checkIfIsLecturerOfCourse(Integer userId, String courseCode, String semester) {
        return jdbcClient.sql("SELECT count(*) FROM LECTURERS where user_id=? and COURSE_CODE=? and SEMESTER=?")
                .param(userId)
                .param(courseCode)
                .param(semester)
                .query(Integer.class)
                .single();
    }
    public Integer checkIfStudentIsInCourse(Integer userId, String courseCode, String semester) {
        return jdbcClient.sql("SELECT count(*) FROM FINAL_GRADES where user_id=? and COURSE_CODE=? and SEMESTER=?")
                .param(userId)
                .param(courseCode)
                .param(semester)
                .query(Integer.class)
                .single();
    }
    public Integer removeUser(Integer userId) {
        return jdbcClient.sql("DELETE FROM USERS where user_id=?")
                .param(userId)
                .update();
    }

}
