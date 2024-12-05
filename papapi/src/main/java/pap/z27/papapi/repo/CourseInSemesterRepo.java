package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.Course;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.Group;

import java.util.List;
@Repository
public class CourseInSemesterRepo {
    @Autowired
    private final JdbcClient jdbcClient;
    public CourseInSemesterRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<CourseInSemester> findAllCoursesInSemester() {
        return jdbcClient.sql("SELECT * from COURSES_IN_SEMESTER")
                .query(CourseInSemester.class)
                .list();
    }
    public List<CourseInSemester> findAllCoursesInSemesterByCode(String course_code) {
        return jdbcClient.sql("SELECT * from COURSES_IN_SEMESTER where course_code = ?")
                .param(course_code)
                .query(CourseInSemester.class)
                .list();
    }
    public List<CourseInSemester> findAllCoursesInSemesterBySemester(String semester) {
        return jdbcClient.sql("SELECT * from COURSES_IN_SEMESTER where semester = ?")
                .param(semester)
                .query(CourseInSemester.class)
                .list();
    }
    public List<CourseInSemester> findAllCoursesInTheSemesterCoordinators(String semester, Integer userID) {
        return jdbcClient.sql("SELECT * from COURSES_IN_SEMESTER cis join COORDINATORS c ON (cis.course_code=c.course_code and cis.semester=c.semester) where c.user_id=? and cis.semester = ?")
                .param(userID)
                .param(semester)
                .query(CourseInSemester.class)
                .list();
    }
    public Integer insertCourseInSemester(CourseInSemester courseInSemester) {
        return jdbcClient.sql("INSERT INTO COURSES_IN_SEMESTER (course_code, semester) VALUES (?,?)")
                .param(courseInSemester.getCourse_code())
                .param(courseInSemester.getSemester())
                .update();
    }
    public Integer addCoordinator(Integer userID, CourseInSemester courseInSemester) {
        return jdbcClient.sql("INSERT INTO COORDINATORS (user_id,course_code,semester) VALUES (?,?,?)")
                .param(userID)
                .param(courseInSemester.getCourse_code())
                .param(courseInSemester.getSemester())
                .update();
    }
}
