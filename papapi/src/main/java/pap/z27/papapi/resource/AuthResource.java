package pap.z27.papapi.resource;

import jakarta.servlet.http.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.subclasses.Credentials;
import pap.z27.papapi.domain.subclasses.Password;
import pap.z27.papapi.domain.subclasses.UserPublicInfo;
import pap.z27.papapi.repo.AuthRepo;
import pap.z27.papapi.repo.UserRepo;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping("/api/auth")
@AllArgsConstructor

public class AuthResource {
    private AuthRepo loginRepo;
    private UserRepo userRepo;

    @GetMapping("/login")
    public ResponseEntity<String> login(HttpServletRequest request) {
        Credentials credentials = new Credentials();
        credentials.setMail(request.getParameter("mail"));
        credentials.setPassword(request.getParameter("password"));
        if(loginRepo.isPasswordCorrect(credentials.getMail(), new Password(credentials.getPassword()))) {
            HttpSession session = request.getSession(false);
            if (session == null) {
                session = request.getSession(true);
            }
            session.setAttribute("mail", credentials.getMail());
            UserPublicInfo user = userRepo.findUsersInfoByMail(credentials.getMail());
            session.setAttribute("name", user.getName());
            session.setAttribute("surname", user.getSurname());
            session.setAttribute("userId", user.getUser_id());
            session.setAttribute("status", user.getStatus());
            session.setAttribute("loggedIn", true);
            return ResponseEntity.ok("{\"logged\":\"ok\"}");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Bad credentials!\"}");
    }
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        if (session != null)
            session.invalidate();
        return ResponseEntity.ok("{\"logged out\":\"ok\"}");
    }
    @GetMapping("/status")
    public ResponseEntity<String> status(HttpSession session) {
        try{
            if((boolean) session.getAttribute("loggedIn")) {
                return ResponseEntity.ok("{\"loggedIn\":true}");
            }else {
                return ResponseEntity.ok("{\"loggedIn\":false}");
            }
        }catch(Exception e) {
            return ResponseEntity.ok("{\"loggedIn\":false}");
        }
    }
}
