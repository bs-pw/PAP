package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.Semester;

import java.util.List;

@Repository
public class SemesterRepo {
    @Autowired
    private final JdbcClient jdbcClient;

    public SemesterRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    public List<Semester> findAllSemesters() {
        return jdbcClient.sql("SELECT * from SEMESTERS")
                .query(Semester.class)
                .list();
    }
    public Integer insertSemester(Semester semester) {
        return jdbcClient.sql("INSERT INTO SEMESTERS (semester_code, start_date, end_date) VALUES (?,?,?)")
                .param(semester.getSemester_code())
                .param(semester.getStart_date())
                .param(semester.getEnd_date())
                .update();
    }
    public Integer removeSemester(String semester_code){
        return jdbcClient.sql("DELETE FROM SEMESTERS where semester_code=?")
                .param(semester_code)
                .update();
    }

    public Integer updateSemester(Semester semester) {
        return jdbcClient.sql("UPDATE SEMESTERS SET START_DATE=?, END_DATE=?" +
                "WHERE SEMESTER_CODE=?")
                .param(semester.getStart_date())
                .param(semester.getEnd_date())
                .param(semester.getSemester_code())
                .update();
    }
}