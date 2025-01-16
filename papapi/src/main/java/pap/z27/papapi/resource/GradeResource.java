package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.FinalGrade;
import pap.z27.papapi.domain.Grade;
import pap.z27.papapi.domain.GradeCategory;
import pap.z27.papapi.domain.subclasses.GradeDTO;
import pap.z27.papapi.repo.GradeCategoryRepo;
import pap.z27.papapi.repo.GradeRepo;
import pap.z27.papapi.repo.GroupRepo;
import pap.z27.papapi.repo.UserRepo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping("/api/grades")
public class GradeResource {
    public final GradeRepo gradeRepo;
    public final UserRepo userRepo;
    public final GroupRepo groupRepo;
    private final GradeCategoryRepo gradeCategoryRepo;

    @Autowired
    public GradeResource(GradeRepo gradeRepo, UserRepo userRepo, GroupRepo groupRepo, GradeCategoryRepo gradeCategoryRepo) {
        this.gradeRepo = gradeRepo;
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
        this.gradeCategoryRepo = gradeCategoryRepo;
    }

//    private String canUserUpdateGrade(Grade grade, HttpSession session) {
//        Integer userId = (Integer)session.getAttribute("user_id");
//        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
//        if (userTypeId == 3) {
//            return "{\"message\":\"Students cannot manipulate grades\"}";
//        }
//
//        grade.setWho_inserted_id(userId);
//        if (grade.getDate() == null) {
//            grade.setDate(LocalDate.now());
//        }
//
//        if (userTypeId != 0) {
//            // Check if lecturer is inserting a grade for student in his group
//            if (groupRepo.isStudentInLecturerGroup(grade.getUser_id(),
//                    userId,
//                    grade.getSemester(),
//                    grade.getCourse_code()) == 0) {
//                return "{\"message\":\"Lecturer does not lead the student's group.\"}";
//            }
//        }
//       return "ok";
//    }

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
                userRepo.checkIfIsCoordinator(userId, courseCode, semester) == 0) {
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
        if (userRepo.checkIfStudentIsInCourse(userId, courseCode, semester) == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

            if (!userTypeId.equals(0) && !thisUserId.equals(userId) && userRepo.checkIfIsCoordinator(thisUserId, courseCode, semester) == 0 && userRepo.checkIfIsLecturerOfCourse(thisUserId, courseCode, semester) == 0) {
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
        if (!userTypeId.equals(0) && userRepo.checkIfIsCoordinator(thisUserId,courseCode,semester)==0 && userRepo.checkIfIsLecturerOfCourse(thisUserId,courseCode,semester)==0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        int exceptionsCounter=0;
        for(var gradeEntity:grades.entrySet()) {

            Grade grade = gradeEntity.getValue();
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
//                if (grade.getGrade() < 0 && grade.getGrade() > gradeCategoryRepo.getGradeCategory(
//                        grade.getSemester(), grade.getCourse_code(), grade.getCategory_id()
//                ).getMax_grade())
//                    return ResponseEntity.badRequest().body("{\"message\":\"Grade must be in [0, max grade].\"}");

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

//    @PostMapping
//    public ResponseEntity<String> insertGrade(@RequestBody Grade grade, HttpSession session) {
//        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
//        if (userTypeId == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        String status = canUserUpdateGrade(grade, session);
//        if (!status.equals("ok")) {
//            return ResponseEntity.badRequest().body(status);
//        }
//               GradeCategory category = gradeCategoryRepo.getGradeCategory(grade.getSemester(), grade.getCourse_code(), grade.getCategory_id());
//        if (category == null) {
//            return ResponseEntity.badRequest().body("{\"message\":\"Grade category does not exist.\"}");
//        }
//        if(grade.getGrade()<0 && grade.getGrade()>category.getMax_grade())
//            return ResponseEntity.badRequest().body("{\"message\":\"Grade must be in [0, max grade].\"}");
//        try {
//            if(gradeRepo.insertGrade(grade)==0)
//                return ResponseEntity.badRequest().body("{\"message\":\"Grade could not be inserted.\"}");
//        } catch (DataAccessException e) {
//            return ResponseEntity.internalServerError().body("{\"message\":\"Grade could not be inserted.\"}");
//        }
//        return ResponseEntity.ok("{\"message\":\"Grade inserted\"}");
//    }

//    @PutMapping("{semester}/{courseCode}/{categoryId}/{userId}")
//    public ResponseEntity<String> updateGrade(
//            @PathVariable String semester,
//            @PathVariable String courseCode,
//            @PathVariable Integer categoryId,
//            @PathVariable Integer userId,
//            @RequestBody Grade grade,
//            HttpSession session) {
//        String status = canUserUpdateGrade(grade, session);
//        if (!status.equals("ok")) {
//            return ResponseEntity.badRequest().body(status);
//        }
//        if(grade.getGrade()<0 && grade.getGrade()>gradeCategoryRepo.getGradeCategory(
//                grade.getSemester(), grade.getCourse_code(), grade.getCategory_id()
//        ).getMax_grade())
//            return ResponseEntity.badRequest().body("{\"message\":\"Grade must be in [0, max grade].\"}");
//        Integer thisUserId = (Integer) session.getAttribute("user_id");
//        grade.setWho_inserted_id(thisUserId);
//        grade.setDate(LocalDate.now());
//try {
//        if(gradeRepo.updateGrade(semester,courseCode,categoryId,userId, grade)==0)
//            return ResponseEntity.badRequest().body("{\"message\":\"Grade could not be changed.\"}");
//    } catch (DataAccessException e) {
//        return ResponseEntity.internalServerError().body("{\"message\":\"Grade could not be changed.\"}");
//    }
//    }

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
        if (!userTypeId.equals(0) && userRepo.checkIfIsCoordinator(thisUserId,courseCode,semester)==0 && userRepo.checkIfIsLecturerOfCourse(thisUserId,courseCode,semester)==0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
//        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
//        if (userTypeId == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        Grade grade = new Grade(categoryId,courseCode,semester,userId,null,null,null);
//        String status = canUserUpdateGrade(grade, session);
//        if (!status.equals("ok")) {
//            return ResponseEntity.badRequest().body(status);
//        }

        try {
            if(gradeRepo.removeGrade(semester,courseCode,categoryId,userId)==0) {
                return ResponseEntity.badRequest().body("{\"message\":\"Grade could not be deleted\"}");
            }
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().body("{\"message\":\"Grade could not be deleted\"}");
        }
        return ResponseEntity.ok("{\"message\":\"Grade deleted\"}");
    }

//    @GetMapping("{semester}/{courseCode}")
//    public ResponseEntity<List<Grade>> getAllCourseGradesInSemester(@PathVariable String courseCode, @PathVariable String semester, HttpSession session) {
//        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
//        if (userTypeId == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        Integer userId = (Integer) session.getAttribute("user_id");
//
//        if (!userTypeId.equals(0) &&
//            userRepo.checkIfIsCoordinator(userId, courseCode, semester) == null &&
//            groupRepo.isLecturerOfCourse(userId, semester, courseCode) == null) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//        return ResponseEntity.ok(gradeRepo.findAllGradesOfCourse(courseCode, semester));
//    }

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
            userRepo.checkIfIsCoordinator(userId, courseCode, semester) == 0 &&
            groupRepo.isLecturerOfCourse(userId, semester, courseCode) == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(gradeRepo.findGroupsGradesInSemester(semester, courseCode, groupNumber));
    }

    // Getting grades by grade categories is in GradeCategoryResource
}
