package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.MyClass;
import pap.z27.papapi.repo.MyClassRepo;
import pap.z27.papapi.repo.UserRepo;

import java.util.List;

@RestController
@RequestMapping(path = "api/class")
public class ClassResource {
    private final MyClassRepo classRepo;
    private final UserRepo userRepo;

    @Autowired
    public ClassResource(MyClassRepo classRepo, UserRepo userRepo) {
        this.classRepo = classRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<MyClass> getStudentClasses(HttpSession session) {
        String status = (String) session.getAttribute("status");
        if (!status.equals("student")) {
            throw new IllegalStateException("You're not a student.");
        }
        Integer userId = (Integer) session.getAttribute("userId");
        return classRepo.findAllUsersClasses(userId);
    }

    @PostMapping
    public ResponseEntity<String> createNewClass(@RequestBody MyClass myClass) {
        try {
            classRepo.insertClass(myClass);
        }catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"cannot insert class\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
