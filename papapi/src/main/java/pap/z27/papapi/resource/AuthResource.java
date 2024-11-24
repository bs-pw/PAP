package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.subclasses.Credentials;
import pap.z27.papapi.domain.subclasses.Password;
import pap.z27.papapi.repo.AuthRepo;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*")
@RequestMapping("/api/auth")
@AllArgsConstructor

public class AuthResource {
    @Autowired
    private AuthRepo loginRepo;

    @GetMapping("/login")
    public ResponseEntity login(HttpServletRequest request) {
        Credentials credentials = new Credentials();
        credentials.setMail(request.getParameter("mail"));
        credentials.setPassword(request.getParameter("password"));
        if(loginRepo.isPasswordCorrect(credentials.getMail(), new Password(credentials.getPassword()))) return ResponseEntity.ok("{\"logged\":\"ok\"}");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Bad credentials!\"}");
    }
}
