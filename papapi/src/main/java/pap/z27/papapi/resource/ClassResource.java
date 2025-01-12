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
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
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
//TODO: Naprawić !!!
//        String status = (String) session.getAttribute("user_type_id");
//        if (!status.equals("3")) {
//            throw new IllegalStateException("You're not a student.");
//        }
        Integer userId = (Integer) session.getAttribute("user_id");
        return classRepo.findAllUsersClasses(userId);
    }

    @PostMapping
    public ResponseEntity<String> insertClass(@RequestBody MyClass myClass,
                                                 HttpSession session) {
        // DAY
        // 0-6: mon-sun
        // 10-16: mon-sun every 2 weeks (even weeks)
        // 20-26: mon-sun every 2 weeks (odd weeks)

        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && (userRepo.checkIfIsLecturer(userId, myClass.getCourse_code(), myClass.getSemester(), myClass.getGroup_number())==0)
                && userRepo.checkIfIsCoordinator(userId, myClass.getCourse_code(), myClass.getSemester())==0)
        {
            ResponseEntity.badRequest().body("{\"message\":\"only admins, lecturers or coordinators of courses can create classes\"}");
        }

        if(classRepo.insertClass(myClass)==0)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"cannot insert class\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
    @DeleteMapping
    public ResponseEntity<String> removeClass(@RequestBody MyClass myClass,
                                                 HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && (userRepo.checkIfIsLecturer(userId, myClass.getCourse_code(), myClass.getSemester(), myClass.getGroup_number())==0)
                && userRepo.checkIfIsCoordinator(userId, myClass.getCourse_code(), myClass.getSemester())==0)
        {
            ResponseEntity.badRequest().body("{\"message\":\"only admins, lecturers or coordinators of courses can remove classes\"}");
        }
        if(classRepo.removeClass(myClass)==0)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"cannot remove class\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
