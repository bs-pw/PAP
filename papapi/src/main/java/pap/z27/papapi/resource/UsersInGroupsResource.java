package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.subclasses.UserInGroup;
import pap.z27.papapi.repo.GroupRepo;
import pap.z27.papapi.repo.UserRepo;

@RestController
@RequestMapping("/api/usersingroups")
public class UsersInGroupsResource {
    private final GroupRepo groupRepo;
    private final UserRepo userRepo;

    @Autowired
    public UsersInGroupsResource(GroupRepo groupRepo, UserRepo userRepo) {
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
    }

    @PostMapping
    public ResponseEntity<String> addStudentToGroup(@RequestBody UserInGroup userInGroup, HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer coordinatorId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && (userRepo.checkIfIsCoordinator(coordinatorId,
                userInGroup.getCourse_code(), 
                userInGroup.getSemester())==0)) {
            return ResponseEntity.badRequest().body("{\"message\":\"Only admins/coordinators can add students to groups\"}");
        }

        Integer userId = userInGroup.getUser_id();
        if (userRepo.findUsersTypeId(userId) != 3 && userRepo.findUsersTypeId(userId)!= 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"User is not a student.\"}");
        }
        if (userRepo.countUsersFinalGrades(userInGroup)==0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"User is not signed up for this course.\"}");
        groupRepo.addStudentToGroup(userInGroup);
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @PutMapping(path = "{newGroupNr}")
    public ResponseEntity<String> updateUserGroup(@PathVariable("newGroupNr") Integer newGroupNr,
                                                  @RequestBody UserInGroup userInGroup,
                                                  HttpSession session) {
        Integer affectedUsersId = userInGroup.getUser_id();
        Integer affectedUsersTypeId = userRepo.findUsersTypeId(affectedUsersId);

        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId == 3) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Students cannot change groups by themselves.\"}");
        }
        if (userTypeId == 0) {
           if (affectedUsersTypeId == 3) {
                   if(groupRepo.updateStudentsGroup(userInGroup, newGroupNr)==0)
                       return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                           .body("{\"message\":\"Couldn't change student's group.\"}");

               return ResponseEntity.ok("{\"message\":\"ok\"}");
           }
        }
        if (userTypeId==1) {
            if (userRepo.checkIfIsCoordinator(userInGroup)==0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\":\"Lecturer that is not a coordinator of the course cannot change groups.\"}");
            if(groupRepo.updateStudentsGroup(userInGroup, newGroupNr)==0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Couldn't change student's group.\"}");

        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
