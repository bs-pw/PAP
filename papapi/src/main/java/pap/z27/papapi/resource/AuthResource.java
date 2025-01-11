package pap.z27.papapi.resource;

import jakarta.servlet.http.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.subclasses.Credentials;
import pap.z27.papapi.domain.subclasses.Password;
import pap.z27.papapi.domain.subclasses.UserLoginInfo;
import pap.z27.papapi.repo.AuthRepo;
import pap.z27.papapi.repo.UserRepo;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping("/api/auth")
@AllArgsConstructor

public class AuthResource {
    private AuthRepo loginRepo;
    private UserRepo userRepo;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Credentials credentials, HttpServletRequest request) {
        if (loginRepo.isPasswordCorrect(credentials.getMail(), new Password(credentials.getPassword()))) {
            HttpSession session = request.getSession(false);
            if (session == null) {
                session = request.getSession(true);
            }
            session.setAttribute("mail", credentials.getMail());
            UserLoginInfo user = userRepo.findUsersLoginInfoByMail(credentials.getMail());
            session.setAttribute("name", user.getName());
            session.setAttribute("surname", user.getSurname());
            session.setAttribute("user_id", user.getUser_id());
            session.setAttribute("user_type_id", user.getUser_type_id());
            session.setAttribute("loggedIn", true);
            String response = "{\"loggedIn\":true, \"name\":\""+session.getAttribute("name")+"\", \"surname\":\""+session.getAttribute("surname")+"\", \"userTypeId\":\""+session.getAttribute("user_type_id")+"\"}";
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\":\"Złe dane logowania!\"}");
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @GetMapping("/status")
    public ResponseEntity<String> status(HttpSession session) {
        try{
            if((boolean) session.getAttribute("loggedIn")) {
                String response = "{\"loggedIn\":true, \"name\":\""+session.getAttribute("name")+"\", \"surname\":\""+session.getAttribute("surname")+"\", \"userTypeId\":\""+session.getAttribute("user_type_id")+"\"}";
                return ResponseEntity.ok(response);
            }else {
                return ResponseEntity.ok("{\"loggedIn\":false}");
            }
        }catch(Exception e) {
            return ResponseEntity.ok("{\"loggedIn\":false}");
        }
    }
}
