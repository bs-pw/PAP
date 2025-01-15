package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.Grade;
import pap.z27.papapi.domain.GradeCategory;

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

    public List<Grade> getGradesByCategory(String semester, String courseCode, Integer categoryId) {
        return jdbcClient.sql("SELECT * FROM GRADES WHERE SEMESTER = ? AND COURSE_CODE = ? AND CATEGORY_ID = ?")
                .param(semester)
                .param(courseCode)
                .param(categoryId)
                .query(Grade.class)
                .list();
    }

    public List<Grade> findAllGradesOfCourseForUser(String courseCode, String semester, Integer userID) {
        return jdbcClient.sql("SELECT * FROM GRADES WHERE course_code=? and semester=? and user_id=?" )
                .param(courseCode)
                .param(semester)
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
    public Integer updateGrade(String semester, String courseCode,Integer categoryId, Integer userId, Grade grade) {
        StringBuilder query = new StringBuilder("UPDATE GRADES SET ");
        List<Object> params = new java.util.ArrayList<>();
        if (grade.getDescription() != null) {
            query.append("description=?, ");
            params.add(grade.getDescription());
        }
        if (grade.getGrade() != null) {
            query.append("grade=?, ");
            params.add(grade.getGrade());
        }
        if (grade.getDate() != null) {
            query.append("date=?, ");
            params.add(grade.getDate());
        }
        if (grade.getWho_inserted_id() != null) {
            query.append("WHO_INSERTED_ID=?, ");
            params.add(grade.getWho_inserted_id());
        }
        if (params.isEmpty()) return 0;
        else {
            query.setLength(query.length() - 2);
            query.append(" WHERE category_id=? AND course_code=? AND semester=? AND user_id=?");
            params.add(categoryId);
            params.add(courseCode);
            params.add(semester);
            params.add(userId);
            return jdbcClient.sql(query.toString())
                    .params(params)
                    .update();
        }
    }
    public Integer removeGrade(String semester, String courseCode,Integer categoryId, Integer userId) {
        return jdbcClient.sql("DELETE FROM GRADES WHERE USER_ID=? AND COURSE_CODE=? AND SEMESTER=? AND CATEGORY_ID=?")
                .param(userId)
                .param(courseCode)
                .param(semester)
                .param(categoryId)
                .update();
    }
}
