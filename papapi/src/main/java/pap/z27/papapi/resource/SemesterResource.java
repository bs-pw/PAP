package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.ClassType;
import pap.z27.papapi.domain.Semester;
import pap.z27.papapi.repo.SemesterRepo;

import java.util.List;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping("/api/semester")
public class SemesterResource {
    private final SemesterRepo semesterRepo;

    @Autowired
    public SemesterResource(SemesterRepo semesterRepo) {
        this.semesterRepo = semesterRepo;
    }

    @GetMapping
    public List<Semester> getAllSemesters()
    {
        return semesterRepo.findAllSemesters();
    }

    @GetMapping("/{semesterCode}")
    public Semester getSemester(@PathVariable String semesterCode)
    {
        return semesterRepo.getSemester(semesterCode);
    }

    @PostMapping
    public ResponseEntity<String> insertSemester (@RequestBody Semester semester, HttpSession session)
    {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can insert semesters\"}\"");
        }
        if(semesterRepo.insertSemester(semester)==0)
            return ResponseEntity.badRequest().body("{\"message\":\"Couldn't insert semester\"}\"");
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @DeleteMapping
    public ResponseEntity<String> removeSemester (@RequestParam String semesterCode, HttpSession session) {
       Integer userTypeId = (Integer) session.getAttribute("user_type_id");
       if (userTypeId != 0) {
           return ResponseEntity.badRequest().body("{\"message\":\"only admin can delete semesters\"}\"");
       }
       if (semesterRepo.removeSemester(semesterCode) == 0)
           return ResponseEntity.badRequest().body("{\"message\":\"semester couldn't be removed\"}\"");
       return ResponseEntity.ok("{\"message\":\"semester removed\"}\"");
    }

    @PutMapping
    public ResponseEntity<String> updateSemester (@RequestBody Semester semester, HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can update semesters\"}\"");
        }
        if (semesterRepo.updateSemester(semester) == 0)
            return ResponseEntity.badRequest().body("{\"message\":\"semester couldn't be updated\"}\"");
        return ResponseEntity.ok("{\"message\":\"semester updated\"}\"");
    }
}
