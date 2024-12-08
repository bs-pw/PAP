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
    public ResponseEntity<String> addStudentToGroup(@RequestBody UserInGroup userInGroup) {
        Integer userId = userInGroup.getUser_id();
        if (!userRepo.findUsersStatus(userId)
                .getStatus()
                .equals("student")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"User is not a student.\"}");
        }
        if (userRepo.countUsersFinalGrades(userInGroup)==0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"User is not signed for this course.\"}");
        groupRepo.addStudentToGroup(userInGroup);
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @PutMapping(path = "{newGroupNr}")
    public ResponseEntity<String> changeUserGroup(@PathVariable("newGroupNr") Integer newGroupNr,
                                                  @RequestBody UserInGroup userInGroup,
                                                  HttpSession session) {
        Integer affectedUsersId = userInGroup.getUser_id();
        String affectedUsersStatus = userRepo.findUsersStatus(affectedUsersId).getStatus();

        String status = session.getAttribute("status").toString();
        if (status.equals("student")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Students cannot change groups by themselves.\"}");
        }
        if (status.equals("admin")) {
           if (affectedUsersStatus.equals("student")) {
               try {
                   groupRepo.changeStudentsGroup(userInGroup, newGroupNr);
               } catch (Exception e) {
                   return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                           .body("{\"message\":\"Couldn't change student's group.\"}");
               }
               return ResponseEntity.ok("{\"message\":\"ok\"}");
           }
        }
        if (status.equals("teacher")) {
            if (userRepo.checkIfIsCoordinator(userInGroup)==0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\":\"Teacher that is not a coordinator of the course cannot change groups.\"}");
            try {
                groupRepo.changeStudentsGroup(userInGroup, newGroupNr);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\":\"Couldn't change student's group.\"}");
            }
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
