package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.Grade;
import pap.z27.papapi.repo.GradeRepo;
import pap.z27.papapi.repo.GroupRepo;
import pap.z27.papapi.repo.UserRepo;

import java.time.LocalDate;
import java.util.List;

@RestController
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

    private String canUserChangeGrade(Grade grade, HttpSession session) {
        Integer userId = (Integer)session.getAttribute("userId");
        String userStatus = session.getAttribute("status").toString();
        if (userStatus.equals("student")) {
            return "{\"message\":\"Students cannot manipulate grades\"}";
        }

        grade.setWho_inserted_id(userId);
        if (grade.getDate() == null) {
            grade.setDate(LocalDate.now());
        }

        if (!userStatus.equals("admin")) {
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


    @GetMapping(path = "{userId}")
    public List<Grade> getAllGrades(@PathVariable Integer userId) {
        String userStatus = userRepo.findUsersStatus(userId).getStatus();

        if (!userStatus.equals("student")) {
            throw new IllegalStateException("User is not student");
        }
        return gradeRepo.getAllUserGrades(userId);
    }

    @PostMapping
    public ResponseEntity<String> insertGrade(@RequestBody Grade grade, HttpSession session) {
        String status = canUserChangeGrade(grade, session);
        if (!status.equals("ok")) {
            return ResponseEntity.badRequest().body(status);
        }

        if(gradeRepo.insertGrade(grade)==0)
            return ResponseEntity.badRequest().body("{\"message\":\"Grade could not be inserted.\"}");

        return ResponseEntity.ok("{\"message\":\"Grade inserted\"}");
    }

    @PutMapping
    public ResponseEntity<String> changeGrade(@RequestBody Grade grade, HttpSession session) {
        String status = canUserChangeGrade(grade, session);
        if (!status.equals("ok")) {
            return ResponseEntity.badRequest().body(status);
        }

        if(gradeRepo.updateGrade(grade)==0)
            return ResponseEntity.badRequest().body("{\"message\":\"Grade could not be changed.\"}");

        return ResponseEntity.ok("{\"message\":\"Grade changed\"}");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteGrade(@RequestBody Grade grade, HttpSession session) {
        String status = canUserChangeGrade(grade, session);
        if (!status.equals("ok")) {
            return ResponseEntity.badRequest().body(status);
        }

        if(gradeRepo.deleteGrade(grade)==0) {
            return ResponseEntity.badRequest().body("{\"message\":\"Grade could not be deleted\"}");
        }
        return ResponseEntity.ok("{\"message\":\"Grade deleted\"}");
    }
}
