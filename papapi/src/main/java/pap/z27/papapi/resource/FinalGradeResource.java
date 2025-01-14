package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.FinalGrade;
import pap.z27.papapi.domain.Group;
import pap.z27.papapi.domain.subclasses.UserPublicInfo;
import pap.z27.papapi.repo.FinalGradeRepo;
import pap.z27.papapi.repo.GroupRepo;
import pap.z27.papapi.repo.UserRepo;

import java.util.List;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping(path = "api/finalgrades")
public class FinalGradeResource {
    private final FinalGradeRepo finalGradeRepo;
    private final UserRepo userRepo;

    @Autowired
    public FinalGradeResource(FinalGradeRepo finalGradeRepo, UserRepo userRepo) {
        this.finalGradeRepo = finalGradeRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<FinalGrade>> getUsersFinalGrades(@PathVariable("userId") Integer userId,
                                                                   HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer thisUserId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && !thisUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(finalGradeRepo.findAllUsersFinalGrades(userId));
    }

    @GetMapping("{semester}/{courseCode}/available")
    public ResponseEntity<List<UserPublicInfo>> getEligibleCourseStudents(@PathVariable("courseCode") String courseCode, @PathVariable("semester") String semester, HttpSession session) {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        Integer userId = (Integer)session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0 && userRepo.checkIfIsCoordinator(userId,courseCode,semester)==0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(userRepo.findAllEligibleUsersToCourse(new CourseInSemester(courseCode,semester)));
    }


    //    @GetMapping("{semester}/user/{userId}")
//    public ResponseEntity<List<FinalGrade>> getUsersFinalGradesThisSemester(@PathVariable("userId") Integer userId,
//                                                                            @PathVariable("semester") String semester,
//                                                                HttpSession session) {
//        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
//        if (userTypeId == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        Integer thisUserId = (Integer) session.getAttribute("user_id");
//        if (userTypeId != 0 && !thisUserId.equals(userId)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//        return ResponseEntity.ok(finalGradeRepo.findAllUsersFinalGrades(userId));
//    }
    @GetMapping("{semester}/course/{courseCode}")
    public ResponseEntity<List<FinalGrade>> getFinalGradesByCourse(@PathVariable("courseCode") String courseCode,
                                                        @PathVariable("semester") String semester,
                                                        HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer coordinatorId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && (userRepo.checkIfIsCoordinator(coordinatorId,
                courseCode,
                semester)==0)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(finalGradeRepo.findAllFinalGradesInCourse(courseCode, semester));
    }

    @PostMapping
    public ResponseEntity<String> insertFinalGrade(@RequestBody FinalGrade finalGrade,
                                                      HttpSession session) {

        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer coordinatorId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && (userRepo.checkIfIsCoordinator(coordinatorId,
                finalGrade.getCourse_code(),
                finalGrade.getSemester())==0)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Only admins/coordinators can add students to courses\"}");
        }
        if(userRepo.checkIfIsLecturerOfCourse(finalGrade.getUser_id(),finalGrade.getCourse_code(),finalGrade.getSemester())!=0)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"User is already lecturer of this course!\"}");
        if(finalGradeRepo.insertFinalGrade(finalGrade)==0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{\"message\":\"Cannot insert final grade\"}");
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @DeleteMapping
    public ResponseEntity<String> removeFinalGrade(@RequestBody FinalGrade finalGrade,
                                                   HttpSession session) {

        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer coordinatorId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && (userRepo.checkIfIsCoordinator(coordinatorId,
                finalGrade.getCourse_code(),
                finalGrade.getSemester())==0)) {
            return ResponseEntity.badRequest().body("{\"message\":\"Only admins/coordinators can remove students from courses\"}");
        }

        if (finalGradeRepo.removeFinalGrade(finalGrade) == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Final grade not found\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");

    }

    @PutMapping
    public ResponseEntity<String> updateFinalGrade(@RequestBody FinalGrade finalGrade, HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0 && (userRepo.checkIfIsCoordinator(userId,
                finalGrade.getCourse_code(),
                finalGrade.getSemester())==0)) {
            return ResponseEntity.badRequest().body("{\"message\":\"Only admins/coordinators can change final grades \"}");
        }
        if (finalGradeRepo.updateFinalGrade(finalGrade) == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Couldn't update final grade\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
