package pap.z27.papapi.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.Course;
import pap.z27.papapi.domain.Group;
import pap.z27.papapi.repo.CourseRepo;
import pap.z27.papapi.repo.GroupRepo;

@RestController
@RequestMapping(path = "api/group")
public class GroupResource {
    private final GroupRepo groupRepo;

    @Autowired
    public GroupResource(GroupRepo groupRepo) {
        this.groupRepo = groupRepo;
    }

    @PostMapping
    public ResponseEntity<String> createNewGroup(@RequestBody Group group) {
        try {
            groupRepo.insertGroup(group);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Cannot insert group (group might already exist)\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteGroup(@RequestBody Group group) {
        if (groupRepo.removeGroup(group) == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Group not found\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
