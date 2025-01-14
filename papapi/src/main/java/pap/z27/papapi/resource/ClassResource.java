package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.MyClass;
import pap.z27.papapi.domain.subclasses.ClassDTO;
import pap.z27.papapi.repo.MyClassRepo;
import pap.z27.papapi.repo.UserRepo;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping(path = "api/classes")
public class ClassResource {
    private final MyClassRepo classRepo;
    private final UserRepo userRepo;

    @Autowired
    public ClassResource(MyClassRepo classRepo, UserRepo userRepo) {
        this.classRepo = classRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public ResponseEntity<List<ClassDTO>> getAllClasses(HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (userTypeId.equals(0)) {
            return ResponseEntity.ok(classRepo.findAllClasses());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    @GetMapping("{userId}")
    public ResponseEntity<List<ClassDTO>> getUserClasses(@PathVariable("userId") Integer userId, HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer lookUpType = userRepo.findUsersTypeId(userId);
        Integer thisUserId = (Integer) session.getAttribute("user_id");

        if (userId.equals(thisUserId) || userTypeId.equals(0)) {
            return ResponseEntity.ok(classRepo.findAllUsersClasses(userId));
        }
        if (lookUpType.equals(1) || lookUpType.equals(2)) {
            return ResponseEntity.ok(classRepo.findAllLecturersClasses(userId));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping
    public ResponseEntity<String> insertClass(@RequestBody MyClass myClass,
                                                 HttpSession session) {
        // DAY
        // 0-6: mon-sun
        // 10-16: mon-sun every 2 weeks (even weeks)
        // 20-26: mon-sun every 2 weeks (odd weeks)

        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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
    @PutMapping("{semester}/{courseCode}/{groupNr}/{classIdForGroup}")
    public ResponseEntity<String> updateClass(
            @PathVariable("courseCode") String courseCode,
            @PathVariable("semester") String semester,
            @PathVariable("groupNr") Integer groupNr,
            @PathVariable("classIdForGroup") Integer classIdForGroup,
            @RequestBody MyClass myClass,
            HttpSession session
            ){
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && (userRepo.checkIfIsLecturer(userId, myClass.getCourse_code(), myClass.getSemester(), myClass.getGroup_number())==0)
                && userRepo.checkIfIsCoordinator(userId, myClass.getCourse_code(), myClass.getSemester())==0)
        {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"only admins, lecturers or coordinators of courses can update classes\"}");
        }

        if(courseCode==null || semester==null || groupNr==null || classIdForGroup==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"There is no such class\"}");
        myClass.setCourse_code(courseCode);
        myClass.setSemester(semester);
        myClass.setGroup_number(groupNr);
        myClass.setClass_id_for_group(classIdForGroup);
        if(classRepo.updateClass(myClass)==0)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"cannot update class\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");

    }

    @DeleteMapping("{semester}/{courseCode}/{groupNr}/{classIdForGroup}")
    public ResponseEntity<String> removeClass(@PathVariable("courseCode") String courseCode,
                                              @PathVariable("semester") String semester,
                                              @PathVariable("groupNr") Integer groupNr,
                                              @PathVariable("classIdForGroup") Integer classIdForGroup,
                                                 HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && (userRepo.checkIfIsLecturer(userId, courseCode, semester, groupNr)==0)
                && userRepo.checkIfIsCoordinator(userId, courseCode, semester)==0)
        {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"only admins, lecturers or coordinators of courses can remove classes\"}");
        }
        if(classRepo.removeClass(courseCode,semester,groupNr,classIdForGroup)==0)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"cannot remove class\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
