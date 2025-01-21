package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.subclasses.UserPublicInfo;
import pap.z27.papapi.repo.CourseInSemesterRepo;
import pap.z27.papapi.repo.FinalGradeRepo;
import pap.z27.papapi.repo.UserRepo;

import java.util.List;

@Slf4j
@RestController
//@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping(path = "api/courseinsemester")
public class CourseInSemesterResource {
    private final CourseInSemesterRepo courseRepo;
    private final UserRepo userRepo;
    private final CourseInSemesterRepo courseInSemesterRepo;
    private final FinalGradeRepo finalGradeRepo;

    @Autowired
    public CourseInSemesterResource(CourseInSemesterRepo courseRepo, UserRepo userRepo, CourseInSemesterRepo courseInSemesterRepo, FinalGradeRepo finalGradeRepo) {
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;
        this.courseInSemesterRepo = courseInSemesterRepo;
        this.finalGradeRepo = finalGradeRepo;
    }
    @GetMapping
    public List<CourseInSemester> getCoursesInSemester() {
        return courseRepo.findAllCoursesInSemester();
    }
    @GetMapping("/bysemester/{semester}")
    public List<CourseInSemester> getCoursesInSemesterBySemester(@PathVariable String semester) {
        return courseRepo.findAllCoursesInSemesterBySemester(semester);
    }

    @GetMapping("/bycourse/{courseCode}")
    public List<CourseInSemester> getCoursesInSemesterByCourse(@PathVariable String courseCode) {
        return courseRepo.findAllCoursesInSemesterByCode(courseCode);
    }

    @GetMapping("/bycoordinator/{semester}/{userId}")
    public List<CourseInSemester> getCoursesInSemesterByCoordinator(@PathVariable String semester, @PathVariable Integer userId) {
        return courseRepo.findAllCoursesInSemesterByCoordinator(semester, userId);
    }

    @GetMapping("/bylecturer/{semester}/{userId}")
    public List<CourseInSemester> getCoursesInSemesterByLecturerAndCoordinator(@PathVariable String semester, @PathVariable Integer userId) {
        return courseRepo.findAllCoursesInSemesterByLecturerAndCoordinator(semester, userId);
    }
    @GetMapping("{semester}/{courseCode}")
    public ResponseEntity<List<UserPublicInfo>> getUsersInCourse(@PathVariable("courseCode") String courseCode,
                                                                       @PathVariable("semester") String semester,
                                                                       HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer coordinatorId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && !userRepo.checkIfIsCoordinator(coordinatorId,
                courseCode, semester) &&
                !userRepo.checkIfIsLecturerOfCourse(coordinatorId, courseCode, semester)
                ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(courseInSemesterRepo.findAllUsersInCourse(courseCode, semester));
    }
    @GetMapping("{semester}/{courseCode}/protocol")
    public ResponseEntity<String> getProtocol(@PathVariable("courseCode") String courseCode,
                                              @PathVariable("semester") String semester,
                                              HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && !userRepo.checkIfIsCoordinator(userId,courseCode,semester)) {
            return ResponseEntity.badRequest().body("{\"message\":\"only coordinator can see protocols\"}\"");
        }
        try {
            if (!courseRepo.checkIfIsClosed(semester, courseCode))
                return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"Not closed yet!\"}\"");
        }
        catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Cannot get protocol \"}\"");
        }
//        TODO: generate protocol.pdf in repo
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
    @GetMapping("{semester}/{courseCode}/is-closed")
    public ResponseEntity<Boolean> checkIsClosed(@PathVariable("courseCode") String courseCode,
                                                                 @PathVariable("semester") String semester,
                                                                 HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try{
            return ResponseEntity.ok(courseInSemesterRepo.checkIfIsClosed(semester, courseCode));
        }
        catch (DataAccessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PostMapping
    public ResponseEntity<String> insertCourseInSemester(@RequestBody CourseInSemester course, HttpSession session)
    {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (userTypeId != 0) {
        return ResponseEntity.badRequest().body("{\"message\":\"only admin can insert courses\"}\"");
        }

        try {
            if(courseRepo.insertCourseInSemester(course)==0) return ResponseEntity.badRequest().body("{\"message\":\"cannot insert course in the semester\"}");
        } catch (DataAccessException e) {
            log.error("e: ", e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"cannot insert course in the semester\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
    @PutMapping("{semester}/{courseCode}")
    public ResponseEntity<String> closeCourseInSemester(@PathVariable("semester")String semester,
                                                        @PathVariable("courseCode") String courseCode,
                                                        HttpSession session)
    {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && !userRepo.checkIfIsCoordinator(userId,courseCode,semester)) {
            return ResponseEntity.badRequest().body("{\"message\":\"only coordinator can update courses\"}\"");
        }
        int closedNumber=0;
        try {
            closedNumber=courseRepo.closeCourseInSemester(semester,courseCode);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"cannot update course in the semester\"}");
        }
        if(closedNumber>0) {
            try {
                finalGradeRepo.updateFinalGradeNullsToTwosInCourse(semester, courseCode);
            } catch (DataAccessException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"cannot update final grades \"}");
            }
        }
        return ResponseEntity.ok("{\"message\":\"ok. "+closedNumber+ " course/s closed\"}");
    }
    @DeleteMapping
    public ResponseEntity<String> deleteCourseInSemester(@RequestBody CourseInSemester course, HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"only admin can delete courses.\"}\"");
        }

        try {
            if(courseRepo.checkIfIsClosed(course.getSemester(), course.getCourse_code()))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

            if (courseRepo.removeCourseInSemester(course) == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\":\"Course not found\"}");
            }
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"Cannot remove course\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
