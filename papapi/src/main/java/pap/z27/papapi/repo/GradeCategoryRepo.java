package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.GradeCategory;

import java.util.List;

@Repository
public class GradeCategoryRepo {
    @Autowired
    private final JdbcClient jdbcClient;
    public GradeCategoryRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    public List<GradeCategory> findAllCourseGradeCategories(String courseCode, String semester) {
        return jdbcClient.sql("SELECT * FROM GRADE_CATEGORIES WHERE course_code=? and semester=?")
                .param(courseCode)
                .param(semester)
                .query(GradeCategory.class)
                .list();
    }
    public Integer insertGradeCategory(GradeCategory gradeCategory) {
        return jdbcClient.sql("INSERT INTO GRADE_CATEGORIES (category_id,course_code,semester,description,max_grade)"+
                "VALUES (?,?,?,?,?)")
                .param(gradeCategory.getCategory_id())
                .param(gradeCategory.getCourse_code())
                .param(gradeCategory.getSemester())
                .param(gradeCategory.getDescription())
                .param(gradeCategory.getMax_grade())
                .update();
    }
    public Integer removeGradeCategory(GradeCategory gradeCategory) {
        return jdbcClient.sql("DELETE FROM GRADE_CATEGORIES WHERE category_id=? and course_code=? and semester=?")
                .param(gradeCategory.getCategory_id())
                .param(gradeCategory.getCourse_code())
                .param(gradeCategory.getSemester())
                .update();
    }

}
