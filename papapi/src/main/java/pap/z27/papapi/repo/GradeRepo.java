package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.Grade;
import pap.z27.papapi.domain.subclasses.GradeDTO;

import java.util.List;

@Repository
public class GradeRepo {
    @Autowired
    private final JdbcClient jdbcClient;
    public GradeRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<GradeDTO> findGroupsGradesInSemester(String semester, String courseCode, Integer groupNr)
    {
        return jdbcClient.sql("SELECT g.CATEGORY_ID, gd.DESCRIPTION category_description, g.COURSE_CODE, g.SEMESTER, " +
                        "g.USER_ID, u.NAME user_name,u.SURNAME user_surname, g.WHO_INSERTED_ID, w.NAME who_inserted_name," +
                                "w.SURNAME who_inserted_surname, g.GRADE, gd.MAX_GRADE, g.\"date\",g.DESCRIPTION FROM GRADES g join GRADE_CATEGORIES gd on g.CATEGORY_ID=gd.CATEGORY_ID" +
                                " and g.SEMESTER=gd.SEMESTER and g.COURSE_CODE=gd.COURSE_CODE join USERS u on g.USER_ID=u.USER_ID join USERS w " +
                                "on g.WHO_INSERTED_ID=w.USER_ID JOIN STUDENTS_IN_GROUPS sig ON g.user_id = sig.user_id AND g.course_code = sig.course_code AND g.semester = sig.semester " +
                "WHERE g.course_code = ? AND g.semester = ? AND sig.group_number = ?")
                .param(semester)
                .param(courseCode)
                .param(groupNr)
                .query(GradeDTO.class)
                .list();
    }

    public List<GradeDTO> findGradesByCategory(String semester, String courseCode, Integer categoryId) {
        return jdbcClient.sql("SELECT g.CATEGORY_ID, gd.DESCRIPTION category_description, g.COURSE_CODE, g.SEMESTER, " +
                        "g.USER_ID, u.NAME user_name,u.SURNAME user_surname, g.WHO_INSERTED_ID, w.NAME who_inserted_name," +
                                "w.SURNAME who_inserted_surname, g.GRADE, gd.MAX_GRADE, g.\"date\", g.DESCRIPTION FROM GRADES g join GRADE_CATEGORIES gd on g.CATEGORY_ID=gd.CATEGORY_ID" +
                                " and g.SEMESTER=gd.SEMESTER and g.COURSE_CODE=gd.COURSE_CODE join USERS u on g.USER_ID=u.USER_ID join USERS w " +
                                "on g.WHO_INSERTED_ID=w.USER_ID WHERE g.SEMESTER = ? AND g.COURSE_CODE = ? AND g.CATEGORY_ID = ?")
                .param(semester)
                .param(courseCode)
                .param(categoryId)
                .query(GradeDTO.class)
                .list();
    }

    public List<GradeDTO> findGradesOfCourseForUser(String semester, String courseCode, Integer userID) {
        return jdbcClient.sql("SELECT g.CATEGORY_ID, gd.DESCRIPTION category_description, g.COURSE_CODE, g.SEMESTER, " +
                "g.USER_ID, u.NAME user_name,u.SURNAME user_surname, g.WHO_INSERTED_ID, w.NAME who_inserted_name," +
                        "w.SURNAME who_inserted_surname, g.GRADE, gd.MAX_GRADE, g.\"date\", g.DESCRIPTION FROM GRADES g join GRADE_CATEGORIES gd on g.CATEGORY_ID=gd.CATEGORY_ID" +
                        " and g.SEMESTER=gd.SEMESTER and g.COURSE_CODE=gd.COURSE_CODE join USERS u on g.USER_ID=u.USER_ID join USERS w " +
                        "on g.WHO_INSERTED_ID=w.USER_ID WHERE g.SEMESTER=? and g.COURSE_CODE=? and g.USER_ID=?")
                .param(semester)
                .param(courseCode)
                .param(userID)
                .query(GradeDTO.class)
                .list();
    }
    public List<GradeDTO> getUserGrades(Integer userID) {
        return jdbcClient.sql("SELECT g.CATEGORY_ID, gd.DESCRIPTION category_description, g.COURSE_CODE, g.SEMESTER, " +
                        "g.USER_ID, u.NAME user_name,u.SURNAME user_surname, g.WHO_INSERTED_ID, w.NAME who_inserted_name," +
                        "w.SURNAME who_inserted_surname, g.GRADE, gd.MAX_GRADE, g.\"date\", g.DESCRIPTION FROM GRADES g join GRADE_CATEGORIES gd on g.CATEGORY_ID=gd.CATEGORY_ID" +
                        " and g.SEMESTER=gd.SEMESTER and g.COURSE_CODE=gd.COURSE_CODE join USERS u on g.USER_ID=u.USER_ID join USERS w " +
                        "on g.WHO_INSERTED_ID=w.USER_ID WHERE g.USER_ID=?")
                .param(userID)
                .query(GradeDTO.class)
                .list();
    }
    public Integer insertGrade(Grade grade) {
        return jdbcClient.sql("INSERT INTO GRADES (category_id,course_code,semester,user_id,who_inserted_id,grade,\"date\",description) VALUES (?,?,?,?,?,?,DEFAULT,?)")
                .param(grade.getCategory_id())
                .param(grade.getCourse_code())
                .param(grade.getSemester())
                .param(grade.getUser_id())
                .param(grade.getWho_inserted_id())
                .param(grade.getGrade())
                .param(grade.getDescription())
                .update();
    }
    public Integer updateGrade(String semester, String courseCode, Grade grade) {
        StringBuilder query = new StringBuilder("UPDATE GRADES SET \"date\"=DEFAULT, ");
        List<Object> params = new java.util.ArrayList<>();
        if (grade.getDescription() != null) {
            query.append("description=?, ");
            params.add(grade.getDescription());
        }
        if (grade.getGrade() != null) {
            query.append("grade=?, ");
            params.add(grade.getGrade());
        }
        if (grade.getWho_inserted_id() != null) {
            query.append("WHO_INSERTED_ID=?, ");
            params.add(grade.getWho_inserted_id());
        }
        if (params.isEmpty()) return 0;
        else {
            query.setLength(query.length() - 2);
            query.append(" WHERE category_id=? AND course_code=? AND semester=? AND user_id=?");
            params.add(grade.getCategory_id());
            params.add(courseCode);
            params.add(semester);
            params.add(grade.getUser_id());
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
