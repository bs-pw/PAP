package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.FinalGrade;
import pap.z27.papapi.domain.Group;
import pap.z27.papapi.domain.User;
import pap.z27.papapi.domain.subclasses.Password;
import pap.z27.papapi.domain.subclasses.Status;
import pap.z27.papapi.domain.subclasses.UserInGroup;
import pap.z27.papapi.domain.subclasses.UserPublicInfo;

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
    public List<User> findAllUsers() {
        return jdbcClient.sql("SELECT * FROM USERS")
                .query(User.class)
                .list();
    }
    public String findPasswordByMail(String mail) {
        return jdbcClient.sql("SELECT password FROM USERS where mail=?")
                .param(mail)
                .query(Password.class)
                .single()
                .getPassword();
    }
    public UserPublicInfo findUsersInfoByID(Integer userId) {
        return jdbcClient.sql("SELECT user_id, name, surname, mail, status FROM USERS where user_id=?")
                .param(userId)
                .query(UserPublicInfo.class)
                .single();
    }
    public UserPublicInfo findUsersInfoByMail(String mail) {
        return jdbcClient.sql("SELECT user_id, name, surname, mail, status FROM USERS where mail=?")
                .param(mail)
                .query(UserPublicInfo.class)
                .single();
    }
    public List<UserPublicInfo> findAllCoordinators(CourseInSemester courseInSemester) {
        return jdbcClient.sql("SELECT u.user_id,u.name,u.surname,u.mail, u.status from USERS u join COORDINATORS c ON u.user_id = c.user_id where c.course_code = ? and c.semester = ?")
                .param(courseInSemester.getCourse_code())
                .param(courseInSemester.getSemester())
                .query(UserPublicInfo.class)
                .list();
    }
    public List<UserPublicInfo> findAllUsersInGroup(Group group) {
        return jdbcClient.sql("SELECT user_id,u.name,u.surname,u.mail from USERS u join STUDENTS_IN_GROUPS sin using(user_id) WHERE sin.course_code = ? AND sin.semester = ? AND sin.group_number = ?")
                .param(group.getCourse_code())
                .param(group.getSemester())
                .param(group.getGroup_number())
                .query(UserPublicInfo.class)
                .list();
    }
    public List<UserPublicInfo> findAllLecturersInGroup(Group group) {
        return jdbcClient.sql("SELECT u.user_id,u.name,u.surname,u.mail, u.status from USERS u join LECTURERS l on(l.user_id=u.user_id) where l.course_code = ? and l.semester = ? and l.group_number=?")
                .param(group.getCourse_code())
                .param(group.getSemester())
                .param(group.getGroup_number())
                .query(UserPublicInfo.class)
                .list();
    }
    public Integer insertUser(User user) {
        return jdbcClient.sql("INSERT INTO USERS (name, surname, password, mail, status) VALUES (?,?,?,?,?)")
                .param(user.getName())
                .param(user.getSurname())
                .param(user.getPassword())
                .param(user.getMail())
                .param(user.getStatus())
                .update();
    }

    public Integer updateUsersPassword(Integer userId, String password) {
        return jdbcClient.sql("UPDATE USERS set password=? where user_id=?")
                .param(password)
                .param(userId)
                .update();
    }
    public Integer updateUsersStatus(Integer userId, String status) {
        return jdbcClient.sql("UPDATE USERS set status=? where user_id=?")
                .param(status)
                .param(userId)
                .update();
    }

    public Status findUsersStatus(Integer userId) {
        return jdbcClient.sql("SELECT status from USERS where USER_ID=?")
                .param(userId)
                .query(Status.class)
                .single();
    }
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
}
