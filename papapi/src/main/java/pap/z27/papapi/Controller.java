package pap.z27.papapi;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domains.User;

import java.util.List;

@RestController
@RequestMapping("/api/r")
@AllArgsConstructor
public class Controller {
    @Autowired
//    private final Services services;
    private PAPRepo papRepo;

    @GetMapping("/u/g")
    List<User> getAllUsers() {
        return papRepo.findAllUsers();
    }
    @GetMapping("/p/{mail}")
    String getpass(@PathVariable String mail) {
        return papRepo.findPasswordByMail(mail);
    }


}
