package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
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

    public GradeCategory getGradeCategory(String semester, String courseCode, String gradeCategory) {
        return jdbcClient.sql("SELECT * FROM GRADE_CATEGORIES WHERE SEMESTER=? and COURSE_CODE=? and CATEGORY_ID=?")
                .param(semester)
                .param(courseCode)
                .param(gradeCategory)
                .query(GradeCategory.class).single();
    }

    public Integer insertGradeCategory(GradeCategory gradeCategory) {
        return jdbcClient.sql("INSERT INTO GRADE_CATEGORIES (category_id,course_code,semester,description,max_grade)" +
                        "VALUES (DEFAULT,?,?,?,?)")
                .param(gradeCategory.getCourse_code())
                .param(gradeCategory.getSemester())
                .param(gradeCategory.getDescription())
                .param(gradeCategory.getMax_grade())
                .update();
    }


    public Integer removeGradeCategory(String course_code, String semester, Integer category_id) {
        return jdbcClient.sql("DELETE FROM GRADE_CATEGORIES WHERE category_id=? and course_code=? and semester=?")
                .param(category_id)
                .param(course_code)
                .param(semester)
                .update();
    }


    public Integer updateGradeCategory(Integer categoryId, String course_code, String semester,
                                       GradeCategory gradeCategory) {
        StringBuilder query = new StringBuilder("UPDATE GRADE_CATEGORIES SET ");
        List<Object> params = new java.util.ArrayList<>();
        if (gradeCategory.getDescription() != null) {
            query.append("description=?, ");
            params.add(gradeCategory.getDescription());
        }
        if (gradeCategory.getMax_grade() != null) {
            query.append("max_grade=?, ");
            params.add(gradeCategory.getMax_grade());
        }
        if (params.isEmpty()) return 0;
        else {
            query.setLength(query.length() - 2);
            query.append(" WHERE category_id=? AND course_code=? AND semester=?");
            params.add(categoryId);
            params.add(course_code);
            params.add(semester);
            return jdbcClient.sql(query.toString())
                    .params(params)
                    .update();
        }
    }
}
