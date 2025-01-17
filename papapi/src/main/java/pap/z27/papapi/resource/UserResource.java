package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.SecurityConfig;
import pap.z27.papapi.domain.Group;
import pap.z27.papapi.domain.subclasses.UserInfo;
import pap.z27.papapi.domain.subclasses.UserPublicInfo;
import pap.z27.papapi.repo.GroupRepo;
import pap.z27.papapi.repo.UserRepo;
import pap.z27.papapi.domain.User;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping("/api/user")

public class UserResource {
    private final UserRepo userRepo;
    private final GroupRepo groupRepo;
    private final SecurityConfig securityConfig;

    @Autowired
    public UserResource(UserRepo userRepo, GroupRepo groupRepo, SecurityConfig securityConfig) {
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
        this.securityConfig = securityConfig;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserInfo> getUsersInfo(@PathVariable("userId") Integer userId, HttpSession session) {
        Integer thisUserTypeId = (Integer)session.getAttribute("user_type_id");
        Integer thisUserId = (Integer)session.getAttribute("user_id");
        if (thisUserTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer userTypeId = userRepo.findUsersTypeId(userId);
        if (userTypeId == null) {
            return ResponseEntity.notFound().build();
        }
        if(!thisUserTypeId.equals(0) && (userTypeId.equals(3) || userTypeId.equals(4)) && !thisUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UserInfo response = userRepo.findUsersInfoByID(userId);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserPublicInfo>> getAllUsers(HttpSession session) {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!userTypeId.equals(0))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(userRepo.findAllUsers());
    }

    // Do usunięcia???
//    @GetMapping("/password")
//    public String getpass(HttpSession session) {
//        String m=(String)session.getAttribute("mail");
//        return userRepo.findPasswordByMail(m);
//    }

    @PostMapping
    public ResponseEntity<String> insertUser(@RequestBody User user, HttpSession session) {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Only admin can insert users!\"}\"");
        }

        User usr = new User(user.getName(),
                            user.getSurname(),
                            user.getPassword(),
                            user.getMail(),
                            user.getUser_type_id());

        try{
            if(userRepo.insertUser(usr)==0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Couldn't insert user! \"}");
            return ResponseEntity.ok("{\"message\":\"ok\"}");
        }catch(DataAccessException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping(path = "{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable("userId") Integer userId,
            @RequestBody User user,
            HttpSession session
    ) {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        Integer thisUserId = (Integer)session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (userRepo.countUserById(userId) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"ID not found!\"}");
        }
        if(user.getPassword()!=null)
        {
            String hash_pass = securityConfig.passwordEncoder().encode(user.getPassword());
            user.setPassword(hash_pass);
        }

        if (userTypeId == 0) {
            if(userRepo.updateUser(userId, user)==0)
                return ResponseEntity.badRequest().body("{\"message\":\"Incorrect data! \"}\"");
        }
        else if(!thisUserId.equals(userId))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Only admin and user himself can update his profile! \"}\"");
        }
        else {
            try {
                if(userRepo.updateUsersPasswordOrMail(userId, user)==0)
                    return ResponseEntity.badRequest().body("{\"message\":\"Incorrect data! \"}\"");
            } catch (DataAccessException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"Couldn't update user! \"}\"");
            }
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");

    }

    @PutMapping(path = "/type/{userId}")
    public ResponseEntity<String> updateType(
            @PathVariable("userId") Integer userId,
            @RequestParam Integer user_type_id,
            HttpSession session
    ) {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if((Integer)session.getAttribute("user_type_id") == 0) {
            try {
                if(userRepo.updateUsersType(userId, user_type_id)==0)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Bad type id!\"}");
            } catch (DataAccessException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"Couldn't update user type!\"}");
            }
            return ResponseEntity.ok("{\"message\":\"ok\"}");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"No Permission!\"}");
    }

    @GetMapping("/usergroups")
    public ResponseEntity<List<Group>> getUserGroups(HttpSession session) {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer userId = (Integer)session.getAttribute("user_id");
        return ResponseEntity.ok(groupRepo.findAllUsersGroups(userId));
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<String> removeUser(@PathVariable("userId") Integer userId, HttpSession session) {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"Only admin can remove users!\"}\"");
        }
        if (userRepo.countUserById(userId) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"ID not found!\"}");
        }
        Integer thisUserId = (Integer)session.getAttribute("user_id");
        if(thisUserId.equals(userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Can't remove your own profile! \"}");
        try {
            if(userRepo.removeUser(userId)==0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Couldn't remove user! \"}");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"Couldn't remove user! \"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}

