package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.Course;

import java.util.List;

@Repository
public class CourseRepo {
    @Autowired
    private final JdbcClient jdbcClient;
    public CourseRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Course> findAllCourses() {
        return jdbcClient.sql("SELECT * from COURSES")
                .query(Course.class)
                .list();
    }
    public Course findCourse(String courseCode) {
        return jdbcClient.sql("SELECT * from COURSES where course_code=?")
                .param(courseCode)
                .query(Course.class)
                .optional().orElse(null);
    }
    public String findCourseName(String courseCode) {
        return jdbcClient.sql("SELECT TITLE FROM COURSES WHERE COURSE_CODE=?")
                .param(courseCode)
                .query(String.class)
                .single();
    }
    public Integer insertCourse(Course course) {
        return jdbcClient.sql("INSERT INTO COURSES (course_code, title) VALUES (?,?)")
                .param(course.getCourse_code())
                .param(course.getTitle())
                .update();
    }
    public Integer removeCourse(String course_code) {
        return jdbcClient.sql("DELETE FROM COURSES WHERE course_code=?")
                .param(course_code)
                .update();
    }
    public Integer updateCourse(Course course) {
        return jdbcClient.sql("UPDATE COURSES SET title=? WHERE course_code=?")
                .param(course.getTitle())
                .param(course.getCourse_code())
                .update();
    }
}
