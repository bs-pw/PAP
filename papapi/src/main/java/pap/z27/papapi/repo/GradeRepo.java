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
    public List<Grade> findAllGradesOfCourse(CourseInSemester courseInSemester) {
        return jdbcClient.sql("SELECT * FROM GRADES WHERE course_code=? and semester=?")
                .param(courseInSemester.getCourse_code())
                .param(courseInSemester.getSemester())
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
}
