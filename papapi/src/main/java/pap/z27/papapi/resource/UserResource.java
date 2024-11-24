package pap.z27.papapi.resource;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.repo.UserRepo;
import pap.z27.papapi.domain.User;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor

public class UserResource {
    @Autowired
//    private final Services services;
    private UserRepo papRepo;

    @GetMapping("/all")
    List<User> getAllUsers() {
        return papRepo.findAllUsers();
    }
    @GetMapping("/password/{mail}")
    String getpass(@PathVariable String mail) {
        return papRepo.findPasswordByMail(mail);
    }
}


