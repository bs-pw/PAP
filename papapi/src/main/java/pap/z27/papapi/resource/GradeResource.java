package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.Grade;
import pap.z27.papapi.domain.Lecturer;
import pap.z27.papapi.repo.GradeRepo;
import pap.z27.papapi.repo.LecturerRepo;
import pap.z27.papapi.repo.UserRepo;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeResource {
    public final GradeRepo gradeRepo;
    public final UserRepo userRepo;
    public final LecturerRepo lecturerRepo;

    @Autowired
    public GradeResource(GradeRepo gradeRepo, UserRepo userRepo, LecturerRepo lecturerRepo) {
        this.gradeRepo = gradeRepo;
        this.userRepo = userRepo;
        this.lecturerRepo = lecturerRepo;
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
        Integer userId = (Integer)session.getAttribute("user_id");
        String userStatus = session.getAttribute("status").toString();
        if (userStatus.equals("student")) {
           ResponseEntity.badRequest().body("{\"message\":\"Students cannot insert grades\"}");
        }

        grade.setWho_inserted_id(userId);

        if (!userStatus.equals("admin")) {
            // TODO: correct this if
            List<Lecturer> lecturerGroups = lecturerRepo.getLecturerGroups(userId);
            Lecturer lecturer = new Lecturer(userId, grade.getCourse_code(), grade.getSemester(), 0); //TODO: zastąpić to zero, wstawiłem bo się nie kompilowało
            for (Lecturer group : lecturerGroups) {

            }
            if (userRepo.checkIfIsCoordinator(userId, grade.getCourse_code(), grade.getSemester()) == 0) {
                ResponseEntity.badRequest().body("{\"message\":\"You are not a coordinator of this group\"}");
            }
        }

        gradeRepo.insertGrade(grade);
        return ResponseEntity.ok("{\"message\":\"Grade inserted\"}");
    }
}
