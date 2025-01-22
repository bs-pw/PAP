package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.Group;
import pap.z27.papapi.repo.CourseInSemesterRepo;
import pap.z27.papapi.repo.CourseRepo;
import pap.z27.papapi.repo.GroupRepo;
import pap.z27.papapi.repo.UserRepo;

import java.util.List;

@RestController
//@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping(path = "api/group")
public class GroupResource {
    private final GroupRepo groupRepo;
    private final UserRepo userRepo;
    private final CourseInSemesterRepo courseRepo;

    @Autowired
    public GroupResource(GroupRepo groupRepo, UserRepo userRepo, CourseInSemesterRepo courseRepo) {
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
    }

    @GetMapping("/{semesterId}/{courseId}")
    public List<Group> getAllGroupsInSemesterInCourse(@PathVariable String semesterId,
                                                      @PathVariable String courseId) {
        CourseInSemester courseInSemester = new CourseInSemester(courseId, semesterId);
        return groupRepo.findAllGroupsByCourseInSemester(courseInSemester);
    }

    @PostMapping
    public ResponseEntity<String> insertGroup(@RequestBody Group group,
                                                 HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(userTypeId != 0)
        {
            if(!userRepo.checkIfIsCoordinator(userId,group.getCourse_code(),group.getSemester()))
                return ResponseEntity.badRequest().body("{\"message\":\"Only course coordinator can insert groups \"}");
        }
        try {
            if(courseRepo.checkIfIsClosed(group.getSemester(), group.getCourse_code()))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            if(groupRepo.insertGroup(group)==0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Cannot insert group (group might already exist)\"}");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"Cannot insert group (group might already exist)\"}");
        }

        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @DeleteMapping
    public ResponseEntity<String> removeGroup(@RequestBody Group group, HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(userTypeId != 0)
        {
            if(!userRepo.checkIfIsCoordinator(userId,group.getCourse_code(),group.getSemester()))
                return ResponseEntity.badRequest().body("{\"message\":\"Only course coordinator can remove groups \"}");
        }
        try {
            if(courseRepo.checkIfIsClosed(group.getSemester(), group.getCourse_code()))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            if (groupRepo.removeGroup(group) == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\":\"Group not found\"}");
            }
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"Cannot remove group\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
