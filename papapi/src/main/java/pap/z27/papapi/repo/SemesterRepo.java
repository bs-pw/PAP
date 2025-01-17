package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.Semester;
import pap.z27.papapi.domain.subclasses.SemesterCode;

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
    public Semester getSemester(String semesterCode) {
        return jdbcClient.sql("SELECT * from SEMESTERS where semester_code=?")
                .param(semesterCode)
                .query(Semester.class)
                .optional().orElse(null);
    }

    public List<SemesterCode> getSemestersByCoordinatorAndLecturer(Integer userId) {
        return jdbcClient.sql("SELECT c.SEMESTER from COORDINATORS c  where user_id=? UNION SELECT s.SEMESTER from LECTURERS s  where user_id=?")
                .param(userId)
                .param(userId)
                .query(SemesterCode.class)
                .list();
    }
    public List<SemesterCode> getSemestersByCoordinator(Integer userId) {
        return jdbcClient.sql("SELECT distinct s.SEMESTER from COORDINATORS s  where user_id=?")
                .param(userId)
                .query(SemesterCode.class)
                .list();
    }
    public List<SemesterCode> getSemestersByStudent(Integer userId) {
        return jdbcClient.sql("SELECT distinct fg.SEMESTER from FINAL_GRADES fg where user_id=?")
                .param(userId)
                .query(SemesterCode.class)
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