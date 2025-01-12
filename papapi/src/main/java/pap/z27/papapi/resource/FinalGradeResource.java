package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.FinalGrade;
import pap.z27.papapi.domain.Group;
import pap.z27.papapi.repo.FinalGradeRepo;
import pap.z27.papapi.repo.GroupRepo;
import pap.z27.papapi.repo.UserRepo;

@RestController
@RequestMapping(path = "api/finalgrade")
public class FinalGradeResource {
    private final FinalGradeRepo finalGradeRepo;
    private final UserRepo userRepo;

    @Autowired
    public FinalGradeResource(FinalGradeRepo finalGradeRepo, UserRepo userRepo) {
        this.finalGradeRepo = finalGradeRepo;
        this.userRepo = userRepo;
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
            return ResponseEntity.badRequest().body("{\"message\":\"Only admins/coordinators can add students to courses\"}");
        }

        try {
            finalGradeRepo.insertFinalGrade(finalGrade);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Cannot insert final grade\"}");
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
