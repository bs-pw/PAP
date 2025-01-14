package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.Group;
import pap.z27.papapi.repo.GroupRepo;
import pap.z27.papapi.repo.UserRepo;

import java.util.List;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping(path = "api/group")
public class GroupResource {
    private final GroupRepo groupRepo;
    private final UserRepo userRepo;

    @Autowired
    public GroupResource(GroupRepo groupRepo, UserRepo userRepo) {
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
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
            if(userRepo.checkIfIsCoordinator(userId,group.getCourse_code(),group.getSemester())==0)
                return ResponseEntity.badRequest().body("{\"message\":\"Only course coordinator can insert groups \"}");
        }
            if(groupRepo.insertGroup(group)==0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Cannot insert group (group might already exist)\"}");

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
            if(userRepo.checkIfIsCoordinator(userId,group.getCourse_code(),group.getSemester())==0)
                return ResponseEntity.badRequest().body("{\"message\":\"Only course coordinator can remove groups \"}");
        }
        if (groupRepo.removeGroup(group) == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Group not found\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
