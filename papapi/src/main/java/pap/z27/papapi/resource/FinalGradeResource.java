package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.FinalGrade;
import pap.z27.papapi.domain.Group;
import pap.z27.papapi.domain.subclasses.UserAndFinalGrade;
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
    private final GroupRepo groupRepo;

    @Autowired
    public FinalGradeResource(FinalGradeRepo finalGradeRepo, UserRepo userRepo, GroupRepo groupRepo) {
        this.finalGradeRepo = finalGradeRepo;
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
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
        if (userTypeId != 0 && !userRepo.checkIfIsCoordinator(userId,courseCode,semester)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(userRepo.findAllEligibleUsersToCourse(new CourseInSemester(courseCode,semester)));
    }

    @GetMapping("{semester}/user/{userId}")
    public ResponseEntity<List<FinalGrade>> getUsersFinalGradesInSemester(@PathVariable("userId") Integer userId,
                                                                            @PathVariable("semester") String semester,
                                                                    HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer thisUserId = (Integer) session.getAttribute("user_id");

        if (thisUserId.equals(userId)) {
            if (userTypeId.equals(1)) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(finalGradeRepo.findUsersFinalGradesInSemester(userId, semester));
        }

        if (!userTypeId.equals(0)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(finalGradeRepo.findUsersFinalGradesInSemester(userId, semester));
    }

    @GetMapping("{semester}/course/{courseCode}")
    public ResponseEntity<List<UserAndFinalGrade>> getFinalGradesByCourse(@PathVariable("courseCode") String courseCode,
                                                                          @PathVariable("semester") String semester,
                                                                          HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer coordinatorId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && !userRepo.checkIfIsCoordinator(coordinatorId,
                courseCode,
                semester)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(finalGradeRepo.findAllFinalGradesInCourse(courseCode, semester));
    }

    @PostMapping
    public ResponseEntity<String> addUserToCourse(@RequestBody FinalGrade finalGrade,
                                                      HttpSession session) {

        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer coordinatorId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && !userRepo.checkIfIsCoordinator(coordinatorId,
                finalGrade.getCourse_code(),
                finalGrade.getSemester())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Only admins/coordinators can add students to courses\"}");
        }
        if(userRepo.checkIfIsLecturerOfCourse(finalGrade.getUser_id(),finalGrade.getCourse_code(),finalGrade.getSemester()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"User is already lecturer of this course!\"}");
        try {
            if(finalGradeRepo.insertFinalGrade(finalGrade)==0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Cannot insert final grade\"}");
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().body("{\"message\":\"Cannot insert final grade\"}");
        }
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
        if (userTypeId != 0 && !userRepo.checkIfIsCoordinator(coordinatorId,
                finalGrade.getCourse_code(),
                finalGrade.getSemester())) {
            return ResponseEntity.badRequest().body("{\"message\":\"Only admins/coordinators can remove students from courses\"}");
        }

        try {
            if (finalGradeRepo.removeFinalGrade(finalGrade) == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\":\"Final grade not found\"}");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");

    }

    @PutMapping
    public ResponseEntity<String> updateFinalGrade(
            @RequestBody FinalGrade finalGrade,
            HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0 && !userRepo.checkIfIsCoordinator(userId,
                finalGrade.getCourse_code(),
                finalGrade.getSemester())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Only admins/coordinators can change final grades \"}");
        }
        try {
            if (finalGradeRepo.updateFinalGrade(finalGrade) == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\":\"Couldn't update final grade\"}");
            }
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().body("{\"message\":\"Couldn't update final grade\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
