package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.Attendance;
import pap.z27.papapi.repo.AttendanceRepo;
import pap.z27.papapi.repo.GroupRepo;
import pap.z27.papapi.repo.UserRepo;

import java.time.LocalDate;


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
    @PostMapping
    public ResponseEntity<String> insertAttendance(@RequestBody Attendance attendance, HttpSession session){
        Integer userId=(Integer)session.getAttribute("userId");
        String status=session.getAttribute("status").toString();

        if(status.equals("teacher")) {
            if(groupRepo.isLecturerOfGroup(userId,
                    attendance.getCourse_code(),
                    attendance.getSemester(),
                    attendance.getGroup_number()) == 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Lecturer does not lead the group\"}");

            if (attendance.getDate() == null) {
                return ResponseEntity.badRequest().body("{\"message\":\"Date cannot be null\"}");
            }

            if(groupRepo.isStudentInGroup(userId,
                    attendance.getCourse_code(),
                    attendance.getSemester(),
                    attendance.getGroup_number()) == 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"No such student in the group\"}");

            if(attendanceRepo.insertAttendance(attendance)==0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\" Cannot insert attendances\"}");
            return ResponseEntity.ok("{\"message\":\"ok\"}");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Only lecturers can insert attendances\"}");

    }

    @PutMapping
    public ResponseEntity<String> updateAttendance(@RequestBody Attendance attendance, HttpSession session) {
        Integer userId=(Integer)session.getAttribute("userId");
        String status=session.getAttribute("status").toString();
        if(status.equals("teacher")) {
            if(groupRepo.isLecturerOfGroup(userId,attendance.getCourse_code(),attendance.getSemester(),attendance.getGroup_number()) == 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Lecturer does not lead the group\"}");
            attendance.setWho_inserted_id(userId);
            if(groupRepo.isStudentInGroup(userId,attendance.getCourse_code(),attendance.getSemester(),attendance.getGroup_number()) == 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"No such student in the group\"}");
            if(attendanceRepo.updateAttendance(attendance)==0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\" Cannot update attendances\"}");
            return ResponseEntity.ok("{\"message\":\"ok\"}");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Only lecturers can update attendances\"}");
    }
}
