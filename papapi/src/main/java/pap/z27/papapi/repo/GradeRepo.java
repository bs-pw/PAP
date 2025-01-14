package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.Grade;

import java.util.List;

@Repository
public class GradeRepo {
    @Autowired
    private final JdbcClient jdbcClient;
    public GradeRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    public List<Grade> findAllGradesOfCourse(String courseCode, String semester) {
        return jdbcClient.sql("SELECT * FROM GRADES WHERE course_code=? and semester=?")
                .param(courseCode)
                .param(semester)
                .query(Grade.class)
                .list();
    }

    public List<Grade> getGroupGradesInSemester(String courseCode, String semester, Integer groupNumber)
    {
        return jdbcClient.sql("SELECT g.category_id, g.course_code, g.semester, g.user_id, g.who_inserted_id, " +
                "g.grade, g.\"date\", g.description " +
                "FROM GRADES g JOIN STUDENTS_IN_GROUPS sig ON g.user_id = sig.user_id AND g.course_code = sig.course_code AND g.semester = sig.semester " +
                "WHERE g.course_code = ? AND g.semester = ? AND sig.group_number = ?")
                .param(courseCode)
                .param(semester)
                .param(groupNumber)
                .query(Grade.class)
                .list();
    }

    public List<Grade> findAllGradesOfCourseForUser(CourseInSemester courseInSemester, Integer userID) {
        return jdbcClient.sql("SELECT * FROM GRADES WHERE course_code=? and semester=? and user_id=?" )
                .param(courseInSemester.getCourse_code())
                .param(courseInSemester.getSemester())
                .param(userID)
                .query(Grade.class)
                .list();
    }
    public List<Grade> getAllUserGrades(Integer userID) {
        return jdbcClient.sql("SELECT * FROM GRADES WHERE USER_ID=?")
                .param(userID)
                .query(Grade.class)
                .list();
    }
    public Integer insertGrade(Grade grade) {
        return jdbcClient.sql("INSERT INTO GRADES (category_id,course_code,semester,user_id,who_inserted_id,grade,\"date\",description) VALUES (?,?,?,?,?,?,?,?)")
                .param(grade.getCategory_id())
                .param(grade.getCourse_code())
                .param(grade.getSemester())
                .param(grade.getUser_id())
                .param(grade.getWho_inserted_id())
                .param(grade.getGrade())
                .param(grade.getDate())
                .param(grade.getDescription())
                .update();
    }
    public Integer updateGrade(Grade grade) {
        return jdbcClient.sql("UPDATE GRADES set grade=?,\"date\"=?,description=?, who_inserted_id=? where category_id=? and course_code=? and semester=? and user_id=?")
                .param(grade.getGrade())
                .param(grade.getDate())
                .param(grade.getDescription())
                .param(grade.getWho_inserted_id())
                .param(grade.getCategory_id())
                .param(grade.getCourse_code())
                .param(grade.getSemester())
                .param(grade.getUser_id())
                .update();
    }

    public Integer removeGrade(Grade grade) {
        return jdbcClient.sql("DELETE FROM GRADES WHERE USER_ID=? AND COURSE_CODE=? AND SEMESTER=? AND CATEGORY_ID=?")
                .param(grade.getUser_id())
                .param(grade.getCourse_code())
                .param(grade.getSemester())
                .param(grade.getCategory_id())
                .update();
    }
}
