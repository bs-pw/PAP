package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.Group;
import pap.z27.papapi.repo.CourseInSemesterRepo;

@RestController
@RequestMapping(path = "api/courseinsemester")
public class CourseInSemesterResource {
    private final CourseInSemesterRepo courseRepo;

    @Autowired
    public CourseInSemesterResource(CourseInSemesterRepo courseRepo) {
        this.courseRepo = courseRepo;
    }

    @PostMapping
    public ResponseEntity<String> insertCourseInSemester(@RequestBody CourseInSemester course, HttpSession session)
    {
    Integer userTypeId = (Integer)session.getAttribute("user_type_id");
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


//@DeleteMapping
//    public ResponseEntity<String> deleteCourseInSemester(@RequestBody Group group) {
//        if (courseRepo.removeGroup(group) == 0) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body("{\"message\":\"Group not found\"}");
//        }
//        return ResponseEntity.ok("{\"message\":\"ok\"}");
//    }
}
