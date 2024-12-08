package pap.z27.papapi.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.GradeCategory;
import pap.z27.papapi.repo.GradeCategoryRepo;

import java.util.List;

@RestController
@RequestMapping("/api/gradecategory")
public class GradeCategoryResource {
    public final GradeCategoryRepo gradeCategoryRepo;

    @Autowired
    public GradeCategoryResource(GradeCategoryRepo gradeCategoryRepo) {
        this.gradeCategoryRepo = gradeCategoryRepo;
    }

    @PostMapping
    public ResponseEntity<String> insertGradeCategory(@RequestBody GradeCategory gradeCategory) {

        if (gradeCategoryRepo.insertGradeCategory(gradeCategory) == 0)
        {
            return ResponseEntity.badRequest().body("{\"messsage\":\"Could not insert grade category\"}");
        }
        return ResponseEntity.ok("{\"messsage\":\"grade category inserted\"}");
    }

    @GetMapping
    public List<GradeCategory> findCategoryByCourse(
            @RequestParam String courseCode,
            @RequestParam String semester
    ) {
        return gradeCategoryRepo.findAllCourseGradeCategories(courseCode, semester);
    }
}
