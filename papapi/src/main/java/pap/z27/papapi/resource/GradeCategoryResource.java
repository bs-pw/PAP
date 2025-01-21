package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.GradeCategory;
import pap.z27.papapi.repo.*;

import java.util.List;

@RestController
//@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping("/api/gradecategories")
public class GradeCategoryResource {
    public final GradeCategoryRepo gradeCategoryRepo;
    public final UserRepo userRepo;
    public final GroupRepo groupRepo;
    public final CourseInSemesterRepo courseRepo;

    @Autowired
    public GradeCategoryResource(GradeCategoryRepo gradeCategoryRepo, UserRepo userRepo, GroupRepo groupRepo, CourseInSemesterRepo courseRepo) {
        this.gradeCategoryRepo = gradeCategoryRepo;
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
        this.courseRepo = courseRepo;
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
            if(!userRepo.checkIfIsCoordinator(userId,gradeCategory.getCourse_code(),gradeCategory.getSemester()))
                return ResponseEntity.badRequest().body("{\"message\":\"Only course coordinator can insert grade category \"}");
        }
        try {
            if(courseRepo.checkIfIsClosed(gradeCategory.getSemester(), gradeCategory.getCourse_code()))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            if (gradeCategoryRepo.insertGradeCategory(gradeCategory) == 0)
            {
                return ResponseEntity.badRequest().body("{\"message\":\"Could not insert grade category\"}");
            }
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"Could not insert grade category\"}");
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
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(gradeCategoryRepo.findAllCourseGradeCategories(courseCode, semester));
    }

    @GetMapping("/{semester}/{courseCode}/sum")
    public ResponseEntity<Integer> getMaxPointsToGetInCourse(@PathVariable String semester,
                                                             @PathVariable String courseCode,
                                                             HttpSession session
                                                             )
    {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(gradeCategoryRepo.getGradeCategoriesSumForCourse(semester,courseCode));
    }


    @GetMapping("{semester}/{courseCode}/{categoryId}")
    public ResponseEntity<GradeCategory> getGradeCategory(@PathVariable String semester,
                                                          @PathVariable String courseCode,
                                                          @PathVariable Integer categoryId,
                                                          HttpSession session)
    {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        GradeCategory response = gradeCategoryRepo.getGradeCategory(semester, courseCode, categoryId);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(response);
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
                && !userRepo.checkIfIsCoordinator(
                        userId, course_code, semester))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Only course coordinator or admin can update grade category\"}");
        }

        try {
            if(courseRepo.checkIfIsClosed(semester, course_code))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            if (gradeCategoryRepo.updateGradeCategory(categoryId, course_code, semester, gradeCategory) != 0) {
                return ResponseEntity.ok("{\"message\":\"grade category updated\"}");
            }
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"Could not update grade category\"}");
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
                && !userRepo.checkIfIsCoordinator(
                userId, course_code, semester))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Only course coordinator or admin can update grade category\"}");
        }

        try {
            if(courseRepo.checkIfIsClosed(semester, course_code))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            if (gradeCategoryRepo.removeGradeCategory(course_code, semester, categoryId) != 0) {
                return ResponseEntity.ok("{\"message\":\"grade category deleted\"}");
            }
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"Could not delete grade category\"}");
        }
        return ResponseEntity.badRequest().body("{\"message\":\"Couldn't delete grade category.\"}");
    }
}
