package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.subclasses.UserPublicInfo;
import pap.z27.papapi.repo.CourseInSemesterRepo;
import pap.z27.papapi.repo.UserRepo;

import java.util.List;

@RestController
@RequestMapping("/api/coordinators")
public class CoordinatorResource {
    public final CourseInSemesterRepo courseInSemesterRepo;
    public final UserRepo userRepo;

    @Autowired
    public CoordinatorResource(CourseInSemesterRepo courseInSemesterRepo, UserRepo userRepo) {
        this.courseInSemesterRepo = courseInSemesterRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("{courseCode}/{semester}")
    public List<UserPublicInfo> getCourseCoordinators(@PathVariable("courseCode") String courseCode, @PathVariable("semester") String semester) {

       return userRepo.findAllCourseCoordinators(new CourseInSemester(courseCode,semester));
    }


    @PostMapping(path = "{coordinatorId}")
    public ResponseEntity<String> insertCoordinator(@PathVariable Integer coordinatorId,
                                                    @RequestBody CourseInSemester courseInSemester,
                                                    HttpSession session) {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can insert coordinator\"}\"");
        }

        if (userRepo.findUsersTypeId(coordinatorId)==1 && userRepo.findUsersTypeId(coordinatorId)==2) {
            return ResponseEntity.badRequest().body("{\"message\":\"Only teachers can be coordinators\"}");
        }

        if (userRepo.checkIfStudentIsInCourse(coordinatorId,courseInSemester.getCourse_code(),courseInSemester.getSemester())!=0)
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("{\"message\":\"User is already a student in this course.\"}");


        if (courseInSemesterRepo.insertCoordinator(coordinatorId, courseInSemester) == 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"Couldn't add coordinator\"}");
        }

        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @DeleteMapping(path = "{coordinatorId}")
    public ResponseEntity<String> removeCoordinator(@PathVariable Integer coordinatorId,
                                                    @RequestBody CourseInSemester courseInSemester,
                                                    HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"Only admins can delete coordinators\"}");
        }

        if (courseInSemesterRepo.removeCoordinator(coordinatorId, courseInSemester) == 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"Couldn't delete coordinator\"}");
        }

        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
