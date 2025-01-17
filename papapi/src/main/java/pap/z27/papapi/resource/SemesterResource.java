package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.Semester;
import pap.z27.papapi.domain.subclasses.SemesterCode;
import pap.z27.papapi.repo.SemesterRepo;

import java.util.List;

@RestController
//@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
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
    public ResponseEntity<Semester> getSemester(@PathVariable String semesterCode)
    {
        Semester response = semesterRepo.getSemester(semesterCode);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bylecturer/{userId}")
    public ResponseEntity<List<SemesterCode>> getSemesterByCoordinatorAndLecturer(@PathVariable Integer userId)
    {
        return ResponseEntity.ok(semesterRepo.getSemestersByCoordinatorAndLecturer(userId));
    }
    @GetMapping("/bycoordinator/{userId}")
    public ResponseEntity<List<SemesterCode>> getSemesterByCoordinator(@PathVariable Integer userId)
    {
        return ResponseEntity.ok(semesterRepo.getSemestersByCoordinator(userId));
    }
    @GetMapping("/bystudent/{userId}")
    public ResponseEntity<List<SemesterCode>> getSemesterByStudent(@PathVariable Integer userId)
    {
        return ResponseEntity.ok(semesterRepo.getSemestersByStudent(userId));
    }


    @PostMapping
    public ResponseEntity<String> insertSemester (@RequestBody Semester semester, HttpSession session)
    {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (userTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can insert semesters\"}\"");
        }
        try {
            if(semesterRepo.insertSemester(semester)==0)
                return ResponseEntity.badRequest().body("{\"message\":\"Couldn't insert semester\"}\"");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"Couldn't insert semester\"}\"");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @DeleteMapping
    public ResponseEntity<String> removeSemester (@RequestParam String semesterCode, HttpSession session) {
       Integer userTypeId = (Integer) session.getAttribute("user_type_id");
       if (userTypeId != 0) {
           return ResponseEntity.badRequest().body("{\"message\":\"only admin can delete semesters\"}\"");
       }
        try {
            if (semesterRepo.removeSemester(semesterCode) == 0)
                return ResponseEntity.badRequest().body("{\"message\":\"semester couldn't be removed\"}\"");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"semester couldn't be removed\"}\"");
        }
        return ResponseEntity.ok("{\"message\":\"semester removed\"}");
    }

    @PutMapping
    public ResponseEntity<String> updateSemester (@RequestBody Semester semester, HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can update semesters\"}\"");
        }
        try {
            if (semesterRepo.updateSemester(semester) == 0)
                return ResponseEntity.badRequest().body("{\"message\":\"semester couldn't be updated\"}\"");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"semester couldn't be updated\"}\"");
        }
        return ResponseEntity.ok("{\"message\":\"semester updated\"}");
    }
}
