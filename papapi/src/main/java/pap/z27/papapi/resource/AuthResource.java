package pap.z27.papapi.resource;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pap.z27.papapi.domain.subclasses.Credentials;
import pap.z27.papapi.domain.subclasses.Password;
import pap.z27.papapi.repo.AuthRepo;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor

public class AuthResource {
    @Autowired
    private AuthRepo loginRepo;


    @GetMapping("/login")
    public String login(@RequestBody Credentials credentials) {
//        System.out.println(credentials.getPassword());
        if(loginRepo.isPasswordCorrect(credentials.getMail(), new Password(credentials.getPassword()))) return "OK";
        return "ERROR";
    }
}
