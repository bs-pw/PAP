package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.FinalGrade;
import pap.z27.papapi.domain.Grade;
import pap.z27.papapi.repo.GradeRepo;
import pap.z27.papapi.repo.GroupRepo;
import pap.z27.papapi.repo.UserRepo;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping("/api/grades")
public class GradeResource {
    public final GradeRepo gradeRepo;
    public final UserRepo userRepo;
    public final GroupRepo groupRepo;

    @Autowired
    public GradeResource(GradeRepo gradeRepo, UserRepo userRepo, GroupRepo groupRepo) {
        this.gradeRepo = gradeRepo;
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
    }

    private String canUserUpdateGrade(Grade grade, HttpSession session) {
        Integer userId = (Integer)session.getAttribute("user_id");
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == 3) {
            return "{\"message\":\"Students cannot manipulate grades\"}";
        }

        grade.setWho_inserted_id(userId);
        if (grade.getDate() == null) {
            grade.setDate(LocalDate.now());
        }

        if (userTypeId != 0) {
            // Check if lecturer is inserting a grade for student in his group
            if (groupRepo.isStudentInLecturerGroup(grade.getUser_id(),
                    userId,
                    grade.getSemester(),
                    grade.getCourse_code()) == 0) {
                return "{\"message\":\"Lecturer does not lead the student's group.\"}";
            }
        }
       return "ok";
    }

    @GetMapping
    public ResponseEntity<List<Grade>> getUserGrades(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!(userTypeId.equals(2) || userTypeId.equals(3))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(gradeRepo.getAllUserGrades(userId));
    }

    @GetMapping(path = "{userId}")
    public ResponseEntity<List<Grade>> getAllGrades(@PathVariable Integer userId, HttpSession session){
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer thisUserId = (Integer) session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId.equals(3) && !thisUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(gradeRepo.getAllUserGrades(userId));
    }

    @PostMapping
    public ResponseEntity<String> insertGrade(@RequestBody Grade grade, HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String status = canUserUpdateGrade(grade, session);
        if (!status.equals("ok")) {
            return ResponseEntity.badRequest().body(status);
        }

        if(gradeRepo.insertGrade(grade)==0)
            return ResponseEntity.badRequest().body("{\"message\":\"Grade could not be inserted.\"}");

        return ResponseEntity.ok("{\"message\":\"Grade inserted\"}");
    }

    @PutMapping
    public ResponseEntity<String> updateGrade(@RequestBody Grade grade, HttpSession session) {
        String status = canUserUpdateGrade(grade, session);
        if (!status.equals("ok")) {
            return ResponseEntity.badRequest().body(status);
        }

        if(gradeRepo.updateGrade(grade)==0)
            return ResponseEntity.badRequest().body("{\"message\":\"Grade could not be changed.\"}");

        return ResponseEntity.ok("{\"message\":\"Grade changed\"}");
    }

    @DeleteMapping
    public ResponseEntity<String> removeGrade(@RequestBody Grade grade, HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String status = canUserUpdateGrade(grade, session);
        if (!status.equals("ok")) {
            return ResponseEntity.badRequest().body(status);
        }

        if(gradeRepo.removeGrade(grade)==0) {
            return ResponseEntity.badRequest().body("{\"message\":\"Grade could not be deleted\"}");
        }
        return ResponseEntity.ok("{\"message\":\"Grade deleted\"}");
    }

    @GetMapping("{semester}/{courseCode}")
    public ResponseEntity<List<Grade>> getAllCourseGradesInSemester(@PathVariable String courseCode, @PathVariable String semester, HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer userId = (Integer) session.getAttribute("user_id");

        if (!userTypeId.equals(0) &&
            userRepo.checkIfIsCoordinator(userId, courseCode, semester) == 0 &&
            groupRepo.isLecturerOfCourse(userId, semester, courseCode) == 0) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(gradeRepo.findAllGradesOfCourse(courseCode, semester));
    }

    @GetMapping("{semester}/{courseCode}/{groupNumber}")
    public ResponseEntity<List<Grade>> getGroupGradesInSemester(@PathVariable String courseCode,
                                                                @PathVariable String semester,
                                                                @PathVariable Integer groupNumber,
                                                                HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer userId = (Integer) session.getAttribute("user_id");

        if (!userTypeId.equals(0) &&
            userRepo.checkIfIsCoordinator(userId, courseCode, semester) == 0 &&
            groupRepo.isLecturerOfCourse(userId, semester, courseCode) == 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(gradeRepo.getGroupGradesInSemester(courseCode, semester, groupNumber));
    }

    // Getting grades by grade categories is in GradeCategoryResource
}
