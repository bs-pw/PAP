package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        Integer userId = (Integer)session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(userTypeId != 0)
        {
            if(userRepo.checkIfIsCoordinator(userId,group.getCourse_code(),group.getSemester())==0)
                return ResponseEntity.badRequest().body("{\"message\":\"Only course coordinator can insert lecturers \"}");
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
    public ResponseEntity<List<Group>> getLecturerGroups(
            @PathVariable("lecturerId") Integer lecturerId,
            HttpSession session
            ) {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (groupRepo.getLecturerGroups(lecturerId).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(groupRepo.getLecturerGroups(lecturerId));
    }

    @GetMapping
    public List<UserPublicInfo> getAllLecturers() {
        return groupRepo.getAllLecturers();
    }

    @DeleteMapping
    public ResponseEntity<String> removeLecturer(@RequestBody UserInGroup lecturer, HttpSession session) {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        Integer userId = (Integer)session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(userTypeId != 0)
        {
            if(userRepo.checkIfIsCoordinator(userId,lecturer.getCourse_code(),lecturer.getSemester())==0)
                return ResponseEntity.badRequest().body("{\"message\":\"Only course coordinator can remove lecturers \"}");
        }
        if (groupRepo.removeLecturer(lecturer) == 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"Couldn't delete lecturer (lecturer might not exist).\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
