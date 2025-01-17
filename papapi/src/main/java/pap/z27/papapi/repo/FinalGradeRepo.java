package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.FinalGrade;
import pap.z27.papapi.domain.subclasses.UserAndFinalGrade;
import pap.z27.papapi.domain.subclasses.UserPublicInfo;

import java.util.List;

@Repository
public class FinalGradeRepo {

    @Autowired
    private final JdbcClient jdbcClient;
    public FinalGradeRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    public List<FinalGrade> findAllUsersFinalGrades(Integer userId) {
        return jdbcClient.sql("SELECT * FROM FINAL_GRADES where user_id=?")
                .param(userId)
                .query(FinalGrade.class)
                .list();
    }

    public List<UserAndFinalGrade> findAllFinalGradesInCourse(String courseCode, String semester) {
        return jdbcClient.sql("SELECT u.user_id,u.name,u.surname,fg.grade FROM USERS u join FINAL_GRADES fg on u.user_id=fg.user_id where fg.course_code=? and fg.semester=?")
                .param(courseCode)
                .params(semester)
                .query(UserAndFinalGrade.class)
                .list();
    }
    public Integer insertFinalGrade(FinalGrade finalGrade) {
        return jdbcClient.sql("INSERT INTO FINAL_GRADES (user_id, course_code, semester, grade)VALUES(?,?,?,?)")
                .param(finalGrade.getUser_id())
                .param(finalGrade.getCourse_code())
                .param(finalGrade.getSemester())
                .param(finalGrade.getGrade())
                .update();
    }
    public Integer removeFinalGrade(FinalGrade finalGrade) {
        return jdbcClient.sql("DELETE FROM FINAL_GRADES WHERE user_id=? and course_code=? and semester=?")
                .param(finalGrade.getUser_id())
                .param(finalGrade.getCourse_code())
                .param(finalGrade.getSemester())
                .update();
    }
    public Integer updateFinalGrade(String semester, String courseCode, FinalGrade finalGrade) {
        return jdbcClient.sql("UPDATE FINAL_GRADES set grade=? where user_id=? and course_code=? and semester=?")
                .param(finalGrade.getGrade())
                .param(finalGrade.getUser_id())
                .param(courseCode)
                .param(semester)
                .update();
    }
    public Integer updateFinalGradeNullsToTwosInSemester(String semester){
        return jdbcClient.sql("UPDATE FINAL_GRADES SET GRADE=2.0 WHERE SEMESTER=? and GRADE IS NULL")
                .param(semester)
                .update();
    }

    public List<FinalGrade> findUsersFinalGradesInSemester(Integer userId, String semester) {
        return jdbcClient.sql("SELECT * FROM FINAL_GRADES where user_id=? and semester=?")
                .param(userId)
                .param(semester)
                .query(FinalGrade.class)
                .list();
    }

}
