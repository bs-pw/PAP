package pap.z27.papapi.resource;

import com.lowagie.text.pdf.LayoutProcessor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.Services.ReportService;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.FinalGrade;
import pap.z27.papapi.domain.subclasses.GradeCount;
import pap.z27.papapi.domain.subclasses.NameGrade;
import pap.z27.papapi.domain.subclasses.UserAndFinalGrade;
import pap.z27.papapi.domain.subclasses.UserPublicInfo;
import pap.z27.papapi.repo.CourseInSemesterRepo;
import pap.z27.papapi.repo.CourseRepo;
import pap.z27.papapi.repo.FinalGradeRepo;
import pap.z27.papapi.repo.UserRepo;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping(path = "api/finalgrades")
public class FinalGradeResource {
    private final FinalGradeRepo finalGradeRepo;
    private final UserRepo userRepo;
    private final ReportService reportService;
    private final CourseInSemesterRepo courseRepo;
    private final CourseRepo realCourseRepo;

    @Autowired
    public FinalGradeResource(FinalGradeRepo finalGradeRepo, UserRepo userRepo, ReportService reportService, CourseInSemesterRepo courseRepo, CourseRepo realCourseRepo) {
        this.finalGradeRepo = finalGradeRepo;
        this.userRepo = userRepo;
        this.reportService = reportService;
        this.courseRepo = courseRepo;
        this.realCourseRepo = realCourseRepo;
        LayoutProcessor.enableKernLiga();
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<FinalGrade>> getUsersFinalGrades(@PathVariable("userId") Integer userId,
                                                                   HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer thisUserId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && !thisUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(finalGradeRepo.findAllUsersFinalGrades(userId));
    }

    @PutMapping("{semester}/{courseCode}/protocol")
    public ResponseEntity<String> getFinalGradesProtocol(@PathVariable("courseCode") String courseCode,
                                                       @PathVariable("semester") String semester,
                                                       HttpSession session,
                                                       HttpServletResponse response) throws IOException, DataAccessException
    {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && !userRepo.checkIfIsCoordinator(userId,courseCode,semester)) {
            return ResponseEntity.badRequest().body("{\"message\":\"only coordinator can see protocols\"}\"");
        }
        try {
            if (courseRepo.closeCourse(semester, courseCode)==0)
                return ResponseEntity.badRequest().body("{\"message\":\"Cannot close course\"}");
        } catch (DataAccessException e) {
            return ResponseEntity.badRequest().body("{\"message\":\"Cannot close course\"}");
        }
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + semester + "_" + courseCode + "_" + currentDateTime + "_report.pdf";
        response.setHeader(headerKey, headerValue);

        List<String> coordinators = userRepo.findAllCourseCoordinatorNames(semester, courseCode);
        List<String> lecturers = userRepo.findAllCourseLecturerNames(semester, courseCode);
        List<NameGrade> studentNameGrades = courseRepo.findStudentsNamesGradesInCourse(courseCode, semester);
        String courseTitle = realCourseRepo.findCourseName(courseCode);
        List<GradeCount> gradeCounts = finalGradeRepo.getCourseGradeCount(courseCode, semester);

        try {
            reportService.export(response,
                    semester,
                    courseCode,
                    coordinators,
                    lecturers,
                    studentNameGrades,
                    courseTitle,
                    gradeCounts);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("{\"message\":\"Couldn't generate report\"}");
        }

        return ResponseEntity.ok("{\"message\":\"Report generated.\"}");
    }


    @GetMapping("{semester}/{courseCode}/available")
    public ResponseEntity<List<UserPublicInfo>> getEligibleCourseStudents(@PathVariable("courseCode") String courseCode, @PathVariable("semester") String semester, HttpSession session) {
        Integer userTypeId = (Integer)session.getAttribute("user_type_id");
        Integer userId = (Integer)session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0 && !userRepo.checkIfIsCoordinator(userId,courseCode,semester)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(userRepo.findAllEligibleUsersToCourse(new CourseInSemester(courseCode,semester)));
    }

    @GetMapping("{semester}/student/{userId}")
    public ResponseEntity<List<FinalGrade>> getUsersFinalGradesInSemester(@PathVariable("userId") Integer userId,
                                                                            @PathVariable("semester") String semester,
                                                                    HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer thisUserId = (Integer) session.getAttribute("user_id");

        if (thisUserId.equals(userId) || userTypeId.equals(0)) {
            if (userTypeId.equals(1)) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(finalGradeRepo.findUsersFinalGradesInSemester(userId, semester));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("{semester}/course/{courseCode}")
    public ResponseEntity<List<UserAndFinalGrade>> getFinalGradesByCourse(@PathVariable("courseCode") String courseCode,
                                                                          @PathVariable("semester") String semester,
                                                                          HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer coordinatorId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && !userRepo.checkIfIsCoordinator(coordinatorId,
                courseCode,
                semester)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(finalGradeRepo.findAllFinalGradesInCourse(courseCode, semester));
    }

    @PostMapping
    public ResponseEntity<String> addUserToCourse(@RequestBody FinalGrade finalGrade,
                                                      HttpSession session) {

        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer coordinatorId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && !userRepo.checkIfIsCoordinator(coordinatorId,
                finalGrade.getCourse_code(),
                finalGrade.getSemester())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Only admins/coordinators can add students to courses\"}");
        }
        if(userRepo.checkIfIsLecturerOfCourse(finalGrade.getUser_id(),finalGrade.getCourse_code(),finalGrade.getSemester()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"User is already lecturer of this course!\"}");
        try {
            if(courseRepo.checkIfIsClosed(finalGrade.getSemester(), finalGrade.getCourse_code()))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            if(finalGradeRepo.insertFinalGrade(finalGrade)==0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Cannot insert final grade\"}");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"Cannot insert final grade\"}");
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }

    @DeleteMapping
    public ResponseEntity<String> removeFinalGrade(@RequestBody FinalGrade finalGrade,
                                                   HttpSession session) {

        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer coordinatorId = (Integer) session.getAttribute("user_id");
        if (userTypeId != 0 && !userRepo.checkIfIsCoordinator(coordinatorId,
                finalGrade.getCourse_code(),
                finalGrade.getSemester())) {
            return ResponseEntity.badRequest().body("{\"message\":\"Only admins/coordinators can remove students from courses\"}");
        }

        try {
            if(courseRepo.checkIfIsClosed(finalGrade.getSemester(), finalGrade.getCourse_code()))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            if (finalGradeRepo.removeFinalGrade(finalGrade) == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\":\"Final grade not found\"}");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("{\"message\":\"ok\"}");

    }

    @PutMapping("{semester}/{courseCode}")
    public ResponseEntity<String> updateFinalGrades(
            @PathVariable String semester,
            @PathVariable String courseCode,
            @RequestBody Map<Object, FinalGrade> finalGrades,
            HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer userId = (Integer) session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0 && !userRepo.checkIfIsCoordinator(userId, courseCode, semester)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Only admins/coordinators can change final grades \"}");
        }
        try {
            if (courseRepo.checkIfIsClosed(semester, courseCode))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        int exceptionsCounter=0;
        for(var finalGrade : finalGrades.values()) {
            try {
                finalGradeRepo.updateFinalGrade(semester, courseCode, finalGrade);
            }
            catch (DataAccessException e){exceptionsCounter++;}
        }
        if(exceptionsCounter>0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\" Problem with: "+exceptionsCounter+ " final grades.\"}");
        return ResponseEntity.ok().build();
    }
}
