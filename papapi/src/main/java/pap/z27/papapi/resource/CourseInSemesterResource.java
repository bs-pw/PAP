package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.Group;
import pap.z27.papapi.domain.MyClass;
import pap.z27.papapi.domain.Semester;
import pap.z27.papapi.domain.subclasses.UserAndFinalGrade;
import pap.z27.papapi.domain.subclasses.UserPublicInfo;
import pap.z27.papapi.repo.CourseInSemesterRepo;
import pap.z27.papapi.repo.UserRepo;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping(path = "api/courseinsemester")
public class CourseInSemesterResource {
    private final CourseInSemesterRepo courseRepo;
    private final UserRepo userRepo;
    private final CourseInSemesterRepo courseInSemesterRepo;

    @Autowired
    public CourseInSemesterResource(CourseInSemesterRepo courseRepo, UserRepo userRepo, CourseInSemesterRepo courseInSemesterRepo) {
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;
        this.courseInSemesterRepo = courseInSemesterRepo;
    }
    @GetMapping
    public List<CourseInSemester> getCoursesInSemester() {
        return courseRepo.findAllCoursesInSemester();
    }
    @GetMapping("/bysemester/{semester}")
    public List<CourseInSemester> getCoursesInSemesterBySemester(@PathVariable String semester) {
        return courseRepo.findAllCoursesInSemesterBySemester(semester);
    }

    @GetMapping("/bycourse/{courseCode}")
    public List<CourseInSemester> getCoursesInSemesterByCourse(@PathVariable String courseCode) {
        return courseRepo.findAllCoursesInSemesterByCode(courseCode);
    }

    @GetMapping("/bycoordinator/{semester}/{userId}")
    public List<CourseInSemester> getCoursesInSemesterByCoordinator(@PathVariable String semester, @PathVariable Integer userId) {
        return courseRepo.findAllCoursesInSemesterByCoordinator(semester, userId);
    }

    @GetMapping("/bylecturer/{semester}/{userId}")
    public List<CourseInSemester> getCoursesInSemesterByLecturerAndCoordinator(@PathVariable String semester, @PathVariable Integer userId) {
        return courseRepo.findAllCoursesInSemesterByLecturerAndCoordinator(semester, userId);
    }
    @GetMapping("{semester}/{courseCode}")
    public ResponseEntity<List<UserPublicInfo>> getUsersInCourse(@PathVariable("courseCode") String courseCode,
                                                                       @PathVariable("semester") String semester,
                                                                       HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer coordinatorId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && !userRepo.checkIfIsCoordinator(coordinatorId,
                courseCode, semester) &&
                !userRepo.checkIfIsLecturerOfCourse(coordinatorId, courseCode, semester)
                ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(courseInSemesterRepo.findAllUsersInCourse(courseCode, semester));
    }
    @PostMapping
    public ResponseEntity<String> insertCourseInSemester(@RequestBody CourseInSemester course, HttpSession session)
    {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (userTypeId != 0) {
        return ResponseEntity.badRequest().body("{\"message\":\"only admin can insert courses\"}\"");
        }

        try {
            if(courseRepo.insertCourseInSemester(course)==0) return ResponseEntity.badRequest().body("{\"message\":\"cannot insert course in the semester\"}");
        } catch (DataAccessException e) {
            log.error("e: ", e);
            return ResponseEntity.internalServerError().body("{\"message\":\"cannot insert course in the semester\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCourseInSemester(@RequestBody CourseInSemester course, HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"only admin can delete courses.\"}\"");
        }

        try {
            if (courseRepo.removeCourseInSemester(course) == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\":\"Course not found\"}");
            }
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().body("{\"message\":\"Cannot remove course\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
