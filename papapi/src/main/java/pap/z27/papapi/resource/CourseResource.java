package pap.z27.papapi.resource;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pap.z27.papapi.domain.Course;
import pap.z27.papapi.domain.MyClass;
import pap.z27.papapi.repo.CourseRepo;

@RestController
@RequestMapping(path = "api/course")
public class CourseResource {

    private final CourseRepo courseRepo;

    @Autowired
    public CourseResource(CourseRepo courseRepo) {
        this.courseRepo = courseRepo;
    }

    @PostMapping
    public ResponseEntity<String> createNewCourse(@RequestBody Course course) {
        try {
            courseRepo.insertCourse(course);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"cannot insert course\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}


