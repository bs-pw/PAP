package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.GradeCategory;
import pap.z27.papapi.repo.GradeCategoryRepo;
import pap.z27.papapi.repo.GradeRepo;
import pap.z27.papapi.repo.GroupRepo;
import pap.z27.papapi.repo.UserRepo;

import java.util.List;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping("/api/gradecategories")
public class GradeCategoryResource {
    public final GradeCategoryRepo gradeCategoryRepo;
    public final UserRepo userRepo;
    public final GroupRepo groupRepo;
    private final GradeRepo gradeRepo;

    @Autowired
    public GradeCategoryResource(GradeCategoryRepo gradeCategoryRepo, UserRepo userRepo, GroupRepo groupRepo, GradeRepo gradeRepo) {
        this.gradeCategoryRepo = gradeCategoryRepo;
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
        this.gradeRepo = gradeRepo;
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

    @GetMapping("{semester}/{courseCode}")
    public ResponseEntity<List<GradeCategory>> findCategoryByCourse(
            @PathVariable String semester,
            @PathVariable String courseCode,
            HttpSession session
    ) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(groupRepo.isLecturerOfCourse(userId,semester,courseCode)==0 && !userTypeId.equals(0) && userRepo.checkIfIsCoordinator(userId,courseCode,semester)==0)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(gradeCategoryRepo.findAllCourseGradeCategories(courseCode, semester));
    }

    //TODO: funkcja zwracająca sumę możliwych do uzyskania punktów z przedmiotu w danym semestrze. zapytanie typu /api/gradecategories/{semester}/{courseCode}/sum

    @GetMapping("{semester}/{courseCode}/{categoryId}")
    public ResponseEntity<GradeCategory> getGradesByCategory(@PathVariable String semester,
                                                           @PathVariable String courseCode,
                                                           @PathVariable String categoryId,
                                                           HttpSession session)
    {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(groupRepo.isLecturerOfCourse(userId,semester,courseCode)==0 &&
                !userTypeId.equals(0) &&
                userRepo.checkIfIsCoordinator(userId,courseCode,semester)==0)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(gradeCategoryRepo.getGradeCategory(semester, courseCode, categoryId));
    }


    @PutMapping("{semester}/{course_code}/{categoryId}")
    public ResponseEntity<String> updateGradeCategory(@PathVariable String semester,
                                                      @PathVariable String course_code,
                                                      @PathVariable Integer categoryId,
                                                      @RequestBody GradeCategory gradeCategory,
                                                      HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer userId = (Integer) session.getAttribute("user_id");

        if (!userTypeId.equals(0)
                && (userRepo.checkIfIsCoordinator(
                        userId, course_code, semester)==0))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Only course coordinator or admin can update grade category\"}");
        }

        if (gradeCategoryRepo.updateGradeCategory(categoryId, course_code, semester, gradeCategory) != 0) {
            return ResponseEntity.ok("{\"message\":\"grade category updated\"}");
        }
        return ResponseEntity.badRequest().body("{\"message\":\"Couldn't update grade category.\"}");
    }

    @DeleteMapping("{semester}/{course_code}/{categoryId}")
    public ResponseEntity<String> removeGradeCategory(@PathVariable String semester,
                                                      @PathVariable String course_code,
                                                      @PathVariable Integer categoryId,
                                                      HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer userId = (Integer) session.getAttribute("user_id");

        if (!userTypeId.equals(0)
                && (userRepo.checkIfIsCoordinator(
                userId, course_code, semester)==0))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Only course coordinator or admin can update grade category\"}");
        }

        if (gradeCategoryRepo.removeGradeCategory(course_code, semester, categoryId) != 0) {
            return ResponseEntity.ok("{\"message\":\"grade category deleted\"}");
        }
        return ResponseEntity.badRequest().body("{\"message\":\"Couldn't delete grade category.\"}");
    }
}
