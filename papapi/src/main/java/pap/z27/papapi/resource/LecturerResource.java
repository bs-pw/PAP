package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.Group;
import pap.z27.papapi.domain.Lecturer;
import pap.z27.papapi.domain.User;
import pap.z27.papapi.domain.subclasses.UserInGroup;
import pap.z27.papapi.domain.subclasses.UserPublicInfo;
import pap.z27.papapi.repo.GroupRepo;
import pap.z27.papapi.repo.UserRepo;

import java.util.List;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping("/api/lecturer")
public class LecturerResource {
    public final GroupRepo groupRepo;
    public final UserRepo userRepo;

    @Autowired
    public LecturerResource(GroupRepo groupRepo, UserRepo userRepo) {
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
    }

    @PostMapping(path = "{lecturerId}")
    public ResponseEntity<String> insertLecturer(@PathVariable Integer lecturerId,
                                                     @RequestBody Group group, HttpSession session) {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can add lecturers\"}");
        }
        Integer insertedTypeId = userRepo.findUsersTypeId(lecturerId);
        if (insertedTypeId == 3) {
            return ResponseEntity.badRequest().body("{\"message\":\"students cannot be lecturers\"}");
        }
        else if (insertedTypeId == 4) {
            return ResponseEntity.badRequest().body("{\"message\":\"inactive users cannot be lecturers\"}");
        }
        groupRepo.addLecturerToGroup(lecturerId, group);
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @GetMapping(path = "{lecturerId}")
    public List<Group> getLecturerGroups(
            @PathVariable("lecturerId") Integer lecturerId
            ) {
        if (groupRepo.getLecturerGroups(lecturerId).isEmpty()) {
            throw new IllegalArgumentException("No lecturer groups found for lecturer " + lecturerId);
        }
        return groupRepo.getLecturerGroups(lecturerId);
    }

    @GetMapping
    public List<UserPublicInfo> getAllLecturers() {
        return groupRepo.getAllLecturers();
    }

    @DeleteMapping
    public ResponseEntity<String> removeLecturer(@RequestBody UserInGroup lecturer) {
        if (groupRepo.removeLecturer(lecturer) == 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"Couldn't delete lecturer (lecturer might not exist).\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
