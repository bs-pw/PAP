package pap.z27.papapi.resource;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.repo.UserRepo;
import pap.z27.papapi.domain.User;

import java.util.List;

@RestController
@RequestMapping("/api/r")
@AllArgsConstructor

public class UserResource {
    @Autowired
//    private final Services services;
    private UserRepo papRepo;

    @GetMapping("/u/g")
    List<User> getAllUsers() {
        return papRepo.findAllUsers();
    }
    @GetMapping("/p/{mail}")
    String getpass(@PathVariable String mail) {
        return papRepo.findPasswordByMail(mail);
    }
}


