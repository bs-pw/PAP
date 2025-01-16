package pap.z27.papapi.resource;

import jakarta.servlet.http.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.SecurityConfig;
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
    private SecurityConfig securityConfig;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Credentials credentials, HttpServletRequest request) {
        String hash_pass = userRepo.findPasswordByMail(credentials.getMail());
        if (hash_pass == null) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("{\"message\":\"Złe dane logowania!\"}");
        }
        if (securityConfig.passwordEncoder().matches(credentials.getPassword(), hash_pass)) {
            HttpSession session = request.getSession(false);
            if (session == null) {
                session = request.getSession(true);
            }
            session.setAttribute("mail", credentials.getMail());
            UserLoginInfo user = userRepo.findUsersLoginInfoByMail(credentials.getMail());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("{\"message\":\"Złe dane logowania!\"}");
            }
            session.setAttribute("name", user.getName());
            session.setAttribute("surname", user.getSurname());
            session.setAttribute("user_id", user.getUser_id());
            session.setAttribute("user_type_id", user.getUser_type_id());
            session.setAttribute("loggedIn", true);
            String response = "{\"loggedIn\":true, \"user_id\":\""+session.getAttribute("user_id")+"\", \"name\":\""+session.getAttribute("name")+"\", \"surname\":\""+session.getAttribute("surname")+"\", \"mail\":\""+session.getAttribute("mail")+"\", \"userTypeId\":\""+session.getAttribute("user_type_id")+"\"}";
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
                String response = "{\"loggedIn\":true, \"user_id\":\""+session.getAttribute("user_id")+"\", \"name\":\""+session.getAttribute("name")+"\", \"surname\":\""+session.getAttribute("surname")+"\", \"mail\":\""+session.getAttribute("mail")+"\", \"userTypeId\":\""+session.getAttribute("user_type_id")+"\"}";
                return ResponseEntity.ok(response);
            }else {
                return ResponseEntity.ok("{\"loggedIn\":false}");
            }
        }catch(Exception e) {
            return ResponseEntity.ok("{\"loggedIn\":false}");
        }
    }
}
