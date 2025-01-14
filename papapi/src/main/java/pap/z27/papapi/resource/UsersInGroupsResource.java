package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.subclasses.UserInGroup;
import pap.z27.papapi.domain.subclasses.UserPublicInfo;
import pap.z27.papapi.repo.GroupRepo;
import pap.z27.papapi.repo.UserRepo;

import java.util.List;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping("/api/usersingroups")
public class UsersInGroupsResource {
    private final GroupRepo groupRepo;
    private final UserRepo userRepo;

    @Autowired
    public UsersInGroupsResource(GroupRepo groupRepo, UserRepo userRepo) {
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
    }
    @GetMapping("/{semester}/{courseCode}/{groupNr}/students")
    public ResponseEntity<List<UserPublicInfo>> getAllStudentsInGroup(
        @PathVariable("semester") String semester,
        @PathVariable("courseCode") String courseCode,
        @PathVariable("groupNr") Integer groupNr,
            HttpSession session) {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userTypeId.equals(4) || (userTypeId.equals(3) && groupRepo.isStudentInGroup(userId,semester,courseCode,groupNr)==0)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(groupRepo.findStudentsInGroup(courseCode,semester,groupNr));
    }
@GetMapping("/{semester}/{courseCode}/{groupNr}/lecturers")
public ResponseEntity<List<UserPublicInfo>> getAllLecturersOfGroup(@PathVariable("semester") String semester,
                                                                @PathVariable("courseCode") String courseCode,
                                                                @PathVariable("groupNr") Integer groupNr) {

    return ResponseEntity.ok(groupRepo.findLecturersOfGroup(courseCode,semester,groupNr));
}

    @GetMapping("/{semester}/{courseCode}/{groupNr}/available/student")
    public ResponseEntity<List<UserPublicInfo>> getAllEligibleStudentsToGroup(@PathVariable("semester") String semester,
                                                                              @PathVariable("courseCode") String courseCode,
                                                                              @PathVariable("groupNr") Integer groupNr,
                                                                              HttpSession session) {
        Integer thisUserTypeId = (Integer)session.getAttribute("user_type_id");
        if (thisUserTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer coordinatorId = (Integer) session.getAttribute("user_id");
        if (thisUserTypeId != 0 && (userRepo.checkIfIsCoordinator(coordinatorId,
                courseCode,
                semester)==0)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(groupRepo.findEligibleStudentsToGroup(courseCode,semester,groupNr));
    }
    @PostMapping("/{asWho}")
    public ResponseEntity<String> addStudentToGroup(@PathVariable String asWho, @RequestBody UserInGroup userInGroup, HttpSession session) {
        Integer thisUserTypeId = (Integer)session.getAttribute("user_type_id");
        if (thisUserTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer coordinatorId = (Integer) session.getAttribute("user_id");
        if (thisUserTypeId != 0 && (userRepo.checkIfIsCoordinator(coordinatorId,
                userInGroup.getCourse_code(), 
                userInGroup.getSemester())==0)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Only admins/coordinators can add students to groups\"}");
        }

        Integer userId = userInGroup.getUser_id();
        Integer userTypeId = userRepo.findUsersTypeId(userId);
        switch (asWho){
            case "student":
                if (userTypeId!= 3 && userTypeId!= 2) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("{\"message\":\"User is not a student.\"}");
                }
                if (groupRepo.isLecturerOfGroup(userId,userInGroup.getSemester(),userInGroup.getCourse_code(),userInGroup.getGroup_number())!=0)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("{\"message\":\"User is already a lecturer of this group.\"}");

                if (userRepo.countUsersFinalGrades(userInGroup)==0)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("{\"message\":\"User is not signed up for this course.\"}");
                groupRepo.addStudentToGroup(userInGroup);
                return ResponseEntity.ok("{\"message\":\"ok\"}");
            case "lecturer":
                if (userTypeId != 2 && userTypeId!= 1) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("{\"message\":\"User is not a lecturer.\"}");
                }
                if (groupRepo.isStudentInGroup(userId,userInGroup.getSemester(),userInGroup.getCourse_code(),userInGroup.getGroup_number())!=0)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("{\"message\":\"User is already a student in this group.\"}");

                groupRepo.addLecturerToGroup(userInGroup);
                return ResponseEntity.ok("{\"message\":\"ok\"}");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping(path = "{newGroupNr}")
    public ResponseEntity<String> updateUserGroup(@PathVariable("newGroupNr") Integer newGroupNr,
                                                  @RequestBody UserInGroup userInGroup,
                                                  HttpSession session) {
        Integer affectedUsersId = userInGroup.getUser_id();
        Integer affectedUsersTypeId = userRepo.findUsersTypeId(affectedUsersId);

        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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
    @DeleteMapping("/{semester}/{courseCode}/{groupNr}/{userId}")
    public ResponseEntity<String> updateUserGroup(
            @PathVariable("semester") String semester,
            @PathVariable("courseCode") String courseCode,
            @PathVariable("groupNr") Integer groupNr,
            @PathVariable("userId") Integer userId,
            HttpSession session) {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        Integer thisUserId = (Integer)session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserInGroup uig = new UserInGroup(thisUserId,courseCode,semester,groupNr);
        if (userTypeId != 0 && userRepo.checkIfIsCoordinator(uig)==0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("{\"message\":\"only coordinators can remove students/lectureres from groups.\"}");
        }
        uig.setUser_id(userId);
        int result = groupRepo.removeStudentFromGroup(uig)+groupRepo.removeLecturerFromGroup(uig);
        if(result==0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Couldn't remove user from group.\"}");
        if(result>1)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"rm -rf\"}");

        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

}
