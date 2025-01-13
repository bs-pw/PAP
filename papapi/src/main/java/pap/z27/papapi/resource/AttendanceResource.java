package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.Attendance;
import pap.z27.papapi.domain.subclasses.AttendanceDTO;
import pap.z27.papapi.repo.AttendanceRepo;
import pap.z27.papapi.repo.GroupRepo;
import pap.z27.papapi.repo.UserRepo;

import java.util.List;


@RestController
@RequestMapping("/api/attendance")
public class AttendanceResource {
    public final AttendanceRepo attendanceRepo;
    public final UserRepo userRepo;
    public final GroupRepo groupRepo;

    @Autowired
    public AttendanceResource(AttendanceRepo attendanceRepo, UserRepo userRepo, GroupRepo groupRepo) {
        this.attendanceRepo = attendanceRepo;
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<AttendanceDTO>> getAllStudentsAttendances(@PathVariable("userId") Integer userId, HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer thisUserId = (Integer) session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0 && !thisUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(attendanceRepo.findAllStudentsAttendances(userId));
    }

    @GetMapping("/s/{userId}/{courseCode}/{semester}")
    public ResponseEntity<List<AttendanceDTO>> getAllStudentsAttendancesInCourse(
            @PathVariable("userId") Integer userId,
            @PathVariable("courseCode") String courseCode,
            @PathVariable("semester") String semester,
            HttpSession session
    ) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer thisUserId = (Integer) session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0 && !thisUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(attendanceRepo.findAllStudentsAttendancesInCourse(userId, courseCode, semester));
    }

    @GetMapping("{courseCode}/{semester}/{groupNr}")
    public ResponseEntity<List<AttendanceDTO>> getAllAttendancesInGroup(
            @PathVariable("courseCode") String courseCode,
            @PathVariable("semester") String semester,
            @PathVariable("groupNr") Integer groupNr,
            HttpSession session
    ) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        Integer thisUserId = (Integer) session.getAttribute("user_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0 && groupRepo.isLecturerOfGroup(thisUserId,semester,courseCode,groupNr)==0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(attendanceRepo.findAllAttendancesInGroup(courseCode,semester,groupNr));
    }

    @GetMapping
    public ResponseEntity<List<AttendanceDTO>> getAllAttendances(HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId != 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(attendanceRepo.findAllAttendances());
    }

    @PostMapping
    public ResponseEntity<String> insertAttendance(@RequestBody Attendance attendance, HttpSession session){
        Integer userId=(Integer)session.getAttribute("user_id");
        Integer userTypeId=(Integer)session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userTypeId==1) {
            if(groupRepo.isLecturerOfGroup(userId,
                    attendance.getSemester(),
                    attendance.getCourse_code(),
                    attendance.getGroup_number()) == 0)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Lecturer does not lead the group\"}");

            if (attendance.getDate() == null) {
                return ResponseEntity.badRequest().body("{\"message\":\"Date cannot be null\"}");
            }

            if(groupRepo.isStudentInGroup(attendance.getUser_id(),
                    attendance.getSemester(),
                    attendance.getCourse_code(),
                    attendance.getGroup_number()) == 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"No such student in the group\"}");
            attendance.setWho_inserted_id(userId);
            if(attendanceRepo.insertAttendance(attendance)==0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\" Cannot insert attendances\"}");
            return ResponseEntity.ok("{\"message\":\"ok\"}");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Only lecturers can insert attendances\"}");

    }

    @PutMapping
    public ResponseEntity<String> updateAttendance(@RequestBody Attendance attendance, HttpSession session) {
        Integer userId=(Integer)session.getAttribute("user_id");
        Integer userTypeId=(Integer)session.getAttribute("user_type_id");
        if (userTypeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(userTypeId.equals(1)) {
            if(groupRepo.isLecturerOfGroup(userId,attendance.getSemester(),attendance.getCourse_code(),attendance.getGroup_number()) == 0)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Lecturer does not lead the group\"}");
            if(groupRepo.isStudentInGroup(attendance.getUser_id(),
                    attendance.getSemester(),
                    attendance.getCourse_code(),
                    attendance.getGroup_number()) == 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"No such student in the group\"}");
            attendance.setWho_inserted_id(userId);
            if(attendanceRepo.updateAttendance(attendance)==0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\" Cannot update attendances\"}");
            return ResponseEntity.ok("{\"message\":\"ok\"}");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Only lecturers can update attendances\"}");
    }
}
