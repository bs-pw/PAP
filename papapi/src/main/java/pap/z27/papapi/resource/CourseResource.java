package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.Course;
import pap.z27.papapi.repo.CourseRepo;

import java.util.List;

@Slf4j
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
            log.error("e: ", e);
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
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"only admin can insert courses\"}");
        }
        try {
            if(courseRepo.insertCourse(course)==0) return ResponseEntity.badRequest().body("{\"message\":\"cannot insert course\"}");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"cannot insert course\"}");
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
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"only admin can update courses\"}");
        }
        course.setCourse_code(courseCode);
        try {
            if(courseRepo.updateCourse(course)==0)
                return ResponseEntity.badRequest().body("{\"message\":\"cannot update course\"}");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"cannot update course\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @DeleteMapping("/{courseCode}")
    public ResponseEntity<String> deleteCourse(@PathVariable String courseCode, HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"only admin can remove courses\"}");
        }
        try {
            if(courseRepo.removeCourse(courseCode)==0)
                return ResponseEntity.badRequest().body("{\"message\":\"cannot remove course\"}");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"cannot remove course\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

}


