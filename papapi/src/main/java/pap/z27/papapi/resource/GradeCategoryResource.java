package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.GradeCategory;
import pap.z27.papapi.repo.GradeCategoryRepo;
import pap.z27.papapi.repo.GroupRepo;
import pap.z27.papapi.repo.UserRepo;

import java.util.List;

@RestController
@RequestMapping("/api/gradecategory")
public class GradeCategoryResource {
    public final GradeCategoryRepo gradeCategoryRepo;
    public final UserRepo userRepo;
    public final GroupRepo groupRepo;

    @Autowired
    public GradeCategoryResource(GradeCategoryRepo gradeCategoryRepo, UserRepo userRepo, GroupRepo groupRepo) {
        this.gradeCategoryRepo = gradeCategoryRepo;
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
    }

    @PostMapping
    public ResponseEntity<String> insertGradeCategory(@RequestBody GradeCategory gradeCategory, HttpSession session) {

        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer userId = (Integer) session.getAttribute("user_id");
        if(userTypeId != 0)
        {
            if(userRepo.checkIfIsCoordinator(userId,gradeCategory.getCourse_code(),gradeCategory.getSemester())==0)
                return ResponseEntity.badRequest().body("{\"message\":\"Only course coordinator can insert grade category \"}");
        }
        if (gradeCategoryRepo.insertGradeCategory(gradeCategory) == 0)
        {
            return ResponseEntity.badRequest().body("{\"message\":\"Could not insert grade category\"}");
        }
        return ResponseEntity.ok("{\"message\":\"grade category inserted\"}");
    }

    @GetMapping
    public ResponseEntity<List<GradeCategory>> findCategoryByCourse(
            @RequestParam String courseCode,
            @RequestParam String semester,
            HttpSession session
    ) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(groupRepo.isLecturerOfCourse(userId,semester,courseCode)==0 && !userTypeId.equals(0))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(gradeCategoryRepo.findAllCourseGradeCategories(courseCode, semester));
    }
}
