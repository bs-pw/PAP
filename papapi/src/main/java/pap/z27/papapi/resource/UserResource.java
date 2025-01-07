package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.Group;
import pap.z27.papapi.domain.subclasses.UserDTO;
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
    public List<UserDTO> getAllUsers() {
        return userRepo.findAllUsers();
    }

    @GetMapping("/password")
    public String getpass(HttpSession session) {
        String m=(String)session.getAttribute("mail");
        return userRepo.findPasswordByMail(m);
    }

    @PostMapping
    public ResponseEntity<String> signUp(@RequestBody User user) {
        try {
            userRepo.insertUser(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\""+e.getMessage()+"\"}");
        }
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"No Permission!\"}");
    }

    @GetMapping("/usergroups")
    public List<Group> getUserGroups(HttpSession session) {
        Integer userId = (Integer)session.getAttribute("userId");
        return groupRepo.findAllUsersGroups(userId);
    }
}

