package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.Semester;
import pap.z27.papapi.domain.UserType;
import pap.z27.papapi.repo.UserTypeRepo;

import java.util.List;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping("/api/usertypes")
public class UserTypeResource {
    private final UserTypeRepo userTypeRepo;

    @Autowired
    public UserTypeResource(UserTypeRepo userTypeRepo){this.userTypeRepo=userTypeRepo;}

    @GetMapping
    public List<UserType> getAllUserTypes()
    {
        return userTypeRepo.findAllUserTypes();
    }

    @PostMapping
    public ResponseEntity<String> insertUserType (@RequestBody UserType userType, HttpSession session)
    {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can insert user types\"}\"");
        }
        try {
            if(userTypeRepo.insertUserType(userType)==0)
                return ResponseEntity.badRequest().body("{\"message\":\"Couldn't insert user type\"}\"");
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().body("{\"message\":\"Couldn't insert user type\"}\"");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @PutMapping
    public ResponseEntity<String> updateUserType (@RequestBody UserType userType, HttpSession session)
    {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can update user types\"}\"");
        }
        try {
            if(userTypeRepo.updateUserType(userType)==0)
                return ResponseEntity.badRequest().body("{\"message\":\"Couldn't update user type\"}\"");
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().body("{\"message\":\"Couldn't update user type\"}\"");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
    @DeleteMapping
    public ResponseEntity<String> removeUserType (@RequestParam Integer userTypeId, HttpSession session)
    {
        Integer thisUserTypeId = (Integer)session.getAttribute("user_type_id");
        if (thisUserTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (thisUserTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can remove user types\"}\"");
        }
        try {
            if(userTypeRepo.removeUserType(userTypeId)==0)
                return ResponseEntity.badRequest().body("{\"message\":\"Couldn't remove user type\"}\"");
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().body("{\"message\":\"Couldn't remove user type\"}\"");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
