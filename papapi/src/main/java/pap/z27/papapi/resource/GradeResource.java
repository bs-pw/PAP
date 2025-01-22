package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.Grade;
import pap.z27.papapi.domain.subclasses.GradeDTO;
import pap.z27.papapi.repo.*;

import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping("/api/grades")
public class GradeResource {
    public final GradeRepo gradeRepo;
    public final UserRepo userRepo;
    public final GroupRepo groupRepo;
    private final CourseInSemesterRepo courseRepo;

    @Autowired
    public GradeResource(GradeRepo gradeRepo, UserRepo userRepo, GroupRepo groupRepo, CourseInSemesterRepo courseRepo) {
        this.gradeRepo = gradeRepo;
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
        this.courseRepo = courseRepo;
    }
    @GetMapping("{semester}/{courseCode}/{categoryId}/category")
    public ResponseEntity<List<GradeDTO>> getGradesByCategory(@PathVariable String semester,
                                                              @PathVariable String courseCode,
                                                              @PathVariable Integer categoryId,
                                                              HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (groupRepo.isLecturerOfCourse(userId, semester, courseCode) == null &&
                !userTypeId.equals(0) &&
                !userRepo.checkIfIsCoordinator(userId, courseCode, semester)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(gradeRepo.findGradesByCategory(semester, courseCode, categoryId));
    }


    @GetMapping
    public ResponseEntity<List<GradeDTO>> getUserGrades(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user_id");
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!(userTypeId.equals(2) || userTypeId.equals(3))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(gradeRepo.getUserGrades(userId));
    }

    @GetMapping("{semester}/{courseCode}/{userId}/user")
    public ResponseEntity<List<GradeDTO>> findAllGradesOfCourseForUser(
            @PathVariable String semester,
            @PathVariable String courseCode,
            @PathVariable Integer userId,
            HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer thisUserId = (Integer) session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!userRepo.checkIfStudentIsInCourse(userId, courseCode, semester))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        if (!userTypeId.equals(0) && !thisUserId.equals(userId) && !userRepo.checkIfIsCoordinator(thisUserId, courseCode, semester) && !userRepo.checkIfIsLecturerOfCourse(thisUserId, courseCode, semester)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

            return ResponseEntity.ok(gradeRepo.findGradesOfCourseForUser(semester, courseCode, userId));
        }

    @PostMapping("{semester}/{courseCode}")
    public ResponseEntity<String> insertGrades(
            @PathVariable String semester,
            @PathVariable String courseCode,
            @RequestBody Map<Object,Grade> grades,
            HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer thisUserId = (Integer) session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!userTypeId.equals(0) && !userRepo.checkIfIsCoordinator(thisUserId,courseCode,semester) && !userRepo.checkIfIsLecturerOfCourse(thisUserId,courseCode,semester)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            if (courseRepo.checkIfIsClosed(semester, courseCode))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch (DataAccessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        int exceptionsCounter=0;
        for(var grade:grades.values()) {
            if(grade.getGrade()==null) {
                try {
                    gradeRepo.removeGrade(semester, courseCode, grade.getCategory_id(), grade.getUser_id());
                } catch (DataAccessException e) {
                    exceptionsCounter++;
                }
                continue;
            }
            grade.setWho_inserted_id(thisUserId);
            try {
                if (gradeRepo.updateGrade(semester, courseCode, grade) == 0)
                {
                    grade.setSemester(semester);
                    grade.setCourse_code(courseCode);
                    gradeRepo.insertGrade(grade);
                }
            }
            catch (DataAccessException e){exceptionsCounter++;}
        }
        if(exceptionsCounter>0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\" Problem with: "+exceptionsCounter+ " grades.\"}");
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("{semester}/{courseCode}/{categoryId}/{userId}")
    public ResponseEntity<String> removeGrade(
            @PathVariable String semester,
            @PathVariable String courseCode,
            @PathVariable Integer categoryId,
            @PathVariable Integer userId,
            HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer thisUserId = (Integer) session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!userTypeId.equals(0) && !userRepo.checkIfIsCoordinator(thisUserId,courseCode,semester) && !userRepo.checkIfIsLecturerOfCourse(thisUserId,courseCode,semester)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            if(courseRepo.checkIfIsClosed(semester, courseCode))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            if(gradeRepo.removeGrade(semester,courseCode,categoryId,userId)==0) {
                return ResponseEntity.badRequest().body("{\"message\":\"Grade could not be deleted\"}");
            }
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"Grade could not be deleted\"}");
        }
        return ResponseEntity.ok("{\"message\":\"Grade deleted\"}");
    }

    @GetMapping("{semester}/{courseCode}/{groupNumber}/groups")
    public ResponseEntity<List<GradeDTO>> getGroupGradesInSemester(@PathVariable String courseCode,
                                                                   @PathVariable String semester,
                                                                   @PathVariable Integer groupNumber,
                                                                   HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer userId = (Integer) session.getAttribute("user_id");

        if (!userTypeId.equals(0) &&
            !userRepo.checkIfIsCoordinator(userId, courseCode, semester) &&
            groupRepo.isLecturerOfCourse(userId, semester, courseCode) == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(gradeRepo.findGroupsGradesInSemester(semester, courseCode, groupNumber));
    }
}
