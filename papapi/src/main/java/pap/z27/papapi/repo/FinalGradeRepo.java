package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.FinalGrade;

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
    public Integer updateFinalGrade(FinalGrade finalGrade) {
        return jdbcClient.sql("UPDATE FINAL_GRADES set grade=? where user_id=? and course_code=? and semester=?")
                .param(finalGrade.getGrade())
                .param(finalGrade.getUser_id())
                .param(finalGrade.getCourse_code())
                .param(finalGrade.getSemester())
                .update();
    }

}
