package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.Group;
import pap.z27.papapi.domain.subclasses.UserPublicInfo;
import pap.z27.papapi.repo.GroupRepo;
import pap.z27.papapi.repo.UserRepo;
import pap.z27.papapi.domain.User;

import java.util.List;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping("/api/user")

public class UserResource {
    private final UserRepo userRepo;
    private final GroupRepo groupRepo;

    @Autowired
    public UserResource(UserRepo userRepo, GroupRepo groupRepo) {
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
    }

    @GetMapping("/all")
    public List<UserPublicInfo> getAllUsers() {
        return userRepo.findAllUsers();
    }

    @GetMapping("/password")
    public String getpass(HttpSession session) {
        String m=(String)session.getAttribute("mail");
        return userRepo.findPasswordByMail(m);
    }

    @PostMapping
    public ResponseEntity<String> insertUser(@RequestBody User user, HttpSession session) {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId != 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Only admin can insert users!\"}\"");
        }
            if(userRepo.insertUser(user)==0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Couldn't insert user! \"}");
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @PutMapping(path = "{userId}")
    public ResponseEntity<String> updatePassword(
            @PathVariable("userId") Integer userId,
            @RequestParam String password
    ) {
        if (userRepo.countUserById(userId) == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"ID not found!\"}");
        }
        userRepo.updateUsersPassword(userId, password);
        return ResponseEntity.ok("{\"message\":\"ok\"}");

    }

    @PutMapping(path = "/type/{userId}")
    public ResponseEntity<String> updateType(
            @PathVariable("userId") Integer userId,
            @RequestParam Integer user_type_id,
            HttpSession session
    ) {
        if((Integer)session.getAttribute("user_type_id") == 0) {
            if(userRepo.updateUsersType(userId, user_type_id)==0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Bad type id!\"}");
            return ResponseEntity.ok("{\"message\":\"ok\"}");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"No Permission!\"}");
    }

    @GetMapping("/usergroups")
    public List<Group> getUserGroups(HttpSession session) {
        Integer userId = (Integer)session.getAttribute("user_id");
        return groupRepo.findAllUsersGroups(userId);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<String> removeUser(@PathVariable("userId") Integer userId, HttpSession session) {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"Only admin can remove users!\"}\"");
        }
        if (userRepo.countUserById(userId) == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"ID not found!\"}");
        }
        Integer thisUserId = (Integer)session.getAttribute("user_id");
        if(thisUserId.equals(userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Can't remove your own profile! \"}");
        if(userRepo.removeUser(userId)==0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Couldn't remove user! \"}");
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}

