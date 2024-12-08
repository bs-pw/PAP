package pap.z27.papapi.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.FinalGrade;
import pap.z27.papapi.domain.Group;
import pap.z27.papapi.repo.FinalGradeRepo;
import pap.z27.papapi.repo.GroupRepo;

@RestController
@RequestMapping(path = "api/finalgrade")
public class FinalGradeResource {
    private final FinalGradeRepo finalGradeRepo;

    @Autowired
    public FinalGradeResource(FinalGradeRepo finalGradeRepo) {
        this.finalGradeRepo = finalGradeRepo;
    }

    @PostMapping
    public ResponseEntity<String> createNewFinalGrade(@RequestBody FinalGrade finalGrade) {
        try {
            finalGradeRepo.insertFinalGrade(finalGrade);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Cannot insert final grade\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFinalGrade(@RequestBody FinalGrade finalGrade) {
        if (finalGradeRepo.removeFinalGrade(finalGrade) == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Final grade not found\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");

    }

    @PutMapping
    public ResponseEntity<String> updateFinalGrade(@RequestBody FinalGrade finalGrade) {
        if (finalGradeRepo.updateFinalGrade(finalGrade) == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Couldn't update final grade\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
