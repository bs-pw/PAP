package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.Group;
import pap.z27.papapi.domain.Semester;
import pap.z27.papapi.repo.CourseInSemesterRepo;

import java.util.List;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping(path = "api/courseinsemester")
public class CourseInSemesterResource {
    private final CourseInSemesterRepo courseRepo;

    @Autowired
    public CourseInSemesterResource(CourseInSemesterRepo courseRepo) {
        this.courseRepo = courseRepo;
    }
    @GetMapping
    public List<CourseInSemester> getCoursesInSemester() {
        return courseRepo.findAllCoursesInSemester();
    }
    @GetMapping("/bysemester/{semester}")
    public List<CourseInSemester> getCoursesInSemesterBySemester(@PathVariable String semester) {
        return courseRepo.findAllCoursesInSemesterBySemester(semester);
    }
    @GetMapping("/bycourse/{semesterId}")
    public List<CourseInSemester> getCoursesInSemesterByCourse(@PathVariable String courseCode) {
        return courseRepo.findAllCoursesInSemesterByCode(courseCode);
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
            courseRepo.insertCourseInSemester(course);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"message\":\"cannot insert course in the semester\"}");
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

        if (courseRepo.removeCourseInSemester(course) == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Course not found\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
