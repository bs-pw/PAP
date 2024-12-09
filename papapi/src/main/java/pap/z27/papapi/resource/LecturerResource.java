package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.Lecturer;
import pap.z27.papapi.domain.User;
import pap.z27.papapi.repo.LecturerRepo;
import pap.z27.papapi.repo.UserRepo;

import java.util.List;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping("/api/lecturer")
public class LecturerResource {
    public final LecturerRepo lecturerRepo;
    public final UserRepo userRepo;

    @Autowired
    public LecturerResource(LecturerRepo lecturerRepo, UserRepo userRepo) {
        this.lecturerRepo = lecturerRepo;
        this.userRepo = userRepo;
    }

    @PostMapping
    public ResponseEntity<String> insertLecturer(@RequestBody Lecturer lecturer, HttpSession session) {
        String userStatus = session.getAttribute("status").toString();
        if (!userStatus.equals("admin")) {
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can add lecturers\"}");
        }
        String insertedStatus = userRepo.findUsersStatus(lecturer.getUserId()).toString();
        if (insertedStatus.equals("student")) {
            return ResponseEntity.badRequest().body("{\"message\":\"students cannot be lecturers\"}");
        }

        lecturerRepo.insertLecturer(lecturer);
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @GetMapping(path = "{lecturerId}")
    public List<Lecturer> getLecturerGroups(
            @PathVariable("lecturerId") Integer lecturerId
            ) {
        return lecturerRepo.getLecturerGroups(lecturerId);
    }

    @GetMapping
    public List<User> getAllLecturers() {
        return lecturerRepo.getAllLecturers();
    }
}
