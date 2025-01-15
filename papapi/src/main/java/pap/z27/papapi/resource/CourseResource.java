package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.Course;
import pap.z27.papapi.domain.MyClass;
import pap.z27.papapi.repo.CourseRepo;

import java.util.List;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping(path = "api/course")
public class CourseResource {

    private final CourseRepo courseRepo;

    @Autowired
    public CourseResource(CourseRepo courseRepo) {
        this.courseRepo = courseRepo;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseRepo.findAllCourses());
    }

    @GetMapping("/{courseCode}")
    public ResponseEntity<Course> getCourse(@PathVariable String courseCode) {
        try{
            return ResponseEntity.ok(courseRepo.findCourse(courseCode));
        }catch(DataAccessException e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping
    public ResponseEntity<String> insertCourse(@RequestBody Course course,
                                                  HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can insert courses\"}");
        }
        try {
            courseRepo.insertCourse(course);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"message\":\"cannot insert course\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
    @PutMapping("{courseCode}")
    public ResponseEntity<String> updateCourse(@PathVariable String courseCode,
                                               @RequestBody Course course,
                                               HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can update courses\"}");
        }
        course.setCourse_code(courseCode);
        if(courseRepo.updateCourse(course)==0)
            return ResponseEntity.badRequest().body("{\"message\":\"cannot update course\"}");
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @DeleteMapping("/{courseCode}")
    public ResponseEntity<String> deleteCourse(@PathVariable String courseCode) {
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

}


