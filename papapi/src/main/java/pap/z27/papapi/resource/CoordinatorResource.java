package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.FinalGrade;
import pap.z27.papapi.domain.subclasses.UserPublicInfo;
import pap.z27.papapi.repo.CourseInSemesterRepo;
import pap.z27.papapi.repo.UserRepo;

import java.util.List;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping("/api/coordinators")
public class CoordinatorResource {
    public final CourseInSemesterRepo courseInSemesterRepo;
    public final UserRepo userRepo;

    @Autowired
    public CoordinatorResource(CourseInSemesterRepo courseInSemesterRepo, UserRepo userRepo) {
        this.courseInSemesterRepo = courseInSemesterRepo;
        this.userRepo = userRepo;
    }


    @GetMapping("{semester}/{courseCode}")
    public ResponseEntity<List<UserPublicInfo>> getCourseCoordinators(@PathVariable("courseCode") String courseCode, @PathVariable("semester") String semester) {
       return ResponseEntity.ok(userRepo.findAllCourseCoordinators(new CourseInSemester(courseCode,semester)));
    }

    @GetMapping("{semester}/{courseCode}/amicoordinator/{userId}")
    public ResponseEntity<Boolean> checkIfIsCoordinatorOfCourse(@PathVariable("courseCode") String courseCode, @PathVariable("semester") String semester, @PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(userRepo.checkIfIsCoordinator(userId,courseCode,semester)!=null);
    }

    @GetMapping("{semester}/{courseCode}/available")
    public ResponseEntity<List<UserPublicInfo>> getEligibleCourseCoordinators(@PathVariable("courseCode") String courseCode, @PathVariable("semester") String semester, HttpSession session) {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(userRepo.findAllEligibleCourseCoordinators(new CourseInSemester(courseCode,semester)));
    }

    @PostMapping
    public ResponseEntity<String> insertCoordinator(@RequestBody FinalGrade coordinator,
                                                    HttpSession session) {
        Integer thisUserTypeId = (Integer)session.getAttribute("user_type_id");
        if (thisUserTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (thisUserTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can insert coordinator\"}\"");
        }
        Integer userTypeId =userRepo.findUsersTypeId(coordinator.getUser_id());
        if (userTypeId == null) {
            return ResponseEntity.notFound().build();
        }
        if (userTypeId!=1 && userTypeId!=2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Only teachers can be coordinators\"}");
        }

        if (userRepo.checkIfStudentIsInCourse(coordinator.getUser_id(),coordinator.getCourse_code(),coordinator.getSemester())!=null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("{\"message\":\"User is already a student in this course.\"}");

        CourseInSemester courseInSemester = new CourseInSemester(coordinator.getCourse_code(),coordinator.getSemester());
        try {
            if (courseInSemesterRepo.insertCoordinator(coordinator.getUser_id(), courseInSemester) == 0) {
                return ResponseEntity.badRequest().body("{\"message\":\"Couldn't add coordinator\"}");
            }
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().body("{\"message\":\"Couldn't add coordinator\"}");
        }

        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @DeleteMapping(path = "{semester}/{courseCode}/{coordinatorId}")
    public ResponseEntity<String> removeCoordinator(@PathVariable String semester,
                                                    @PathVariable String courseCode,
                                                    @PathVariable Integer coordinatorId,
                                                    HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"Only admins can delete coordinators\"}");
        }
        try {
            if (courseInSemesterRepo.removeCoordinator(coordinatorId, new CourseInSemester(courseCode,semester)) == 0) {
                return ResponseEntity.badRequest().body("{\"message\":\"Couldn't delete coordinator\"}");
            }
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().body("{\"message\":\"Couldn't delete coordinator\"}");
        }

        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
