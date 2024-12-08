package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.Lecturer;
import pap.z27.papapi.domain.User;

import java.util.List;

@Repository
public class LecturerRepo {
    private final JdbcClient jdbcClient;

    @Autowired
    public LecturerRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Integer insertLecturer(Lecturer lecturer) {
        return jdbcClient.sql("INSERT INTO LECTURERS (USER_ID, COURSE_CODE, SEMESTER, GROUP_NUMBER) VALUES (?,?,?,?)")
                .param(lecturer.getUserId())
                .param(lecturer.getCourseCode())
                .param(lecturer.getSemester())
                .param(lecturer.getGroupNumber())
                .update();
    }

    public Integer deleteLecturer(Lecturer lecturer) {
        return jdbcClient.sql("DELETE FROM LECTURERS WHERE USER_ID=? AND COURSE_CODE=? AND SEMESTER=? AND GROUP_NUMBER=?")
                .param(lecturer.getUserId())
                .param(lecturer.getCourseCode())
                .param(lecturer.getSemester())
                .param(lecturer.getGroupNumber())
                .update();
    }

    public List<Lecturer> getLecturerGroups(Integer userId) {
        return jdbcClient.sql("SELECT USER_ID, COURSE_CODE, SEMESTER, GROUP_NUMBER FROM LECTURERS where USER_ID=?")
                .param(userId)
                .query(Lecturer.class)
                .list();
    }

    public List<User> getAllLecturers() {
        return jdbcClient.sql("SELECT DISTINCT u.USER_ID, NAME, SURNAME, MAIL FROM USERS u INNER JOIN LECTURERS l ON u.USER_ID = l.USER_ID")
                .query(User.class)
                .list();
    }

//    public Integer isLecturerOfGroup(Integer userId, Integer groupNumber) {
//        return jdbcClient.sql("SELECT count(*) FROM LECTURERS where USER_ID=? AND GROUP_NUMBER=?")
//    }
}
