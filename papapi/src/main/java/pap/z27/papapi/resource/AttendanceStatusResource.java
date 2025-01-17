package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.AttendanceStatus;
import pap.z27.papapi.repo.AttendanceStatusRepo;

import java.util.List;

@RestController
//@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
@RequestMapping("/api/attendancestatus")
public class AttendanceStatusResource {
    private final AttendanceStatusRepo attendanceStatusRepo;

    @Autowired
    public AttendanceStatusResource(AttendanceStatusRepo attendanceStatusRepo) {
        this.attendanceStatusRepo = attendanceStatusRepo;
    }

    @PostMapping
    public ResponseEntity<String> insertAttendanceStatus(@RequestBody AttendanceStatus attendanceStatus,
                                                         HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId != 0)
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can insert attendance statuses\"}");
        try {
            if (attendanceStatusRepo.insertAttendanceStatus(attendanceStatus) == 0)
                return ResponseEntity.badRequest().body("{\"message\":\"couldn't insert attendance status\"}");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"couldn't insert attendance status\"}");
        }
        return ResponseEntity.ok("{\"message\":\"inserted attendance status\"}");
    }

    @DeleteMapping
    public ResponseEntity<String> removeAttendanceStatus(@RequestParam Integer statusId, HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId != 0)
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can delete attendance statuses\"}");
        try {
            if (attendanceStatusRepo.removeAttendanceStatus(statusId)==0)
                return ResponseEntity.badRequest().body("{\"message\":\"couldn't delete attendance status\"}");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"couldn't delete attendance status\"}");
        }
        return ResponseEntity.ok("{\"message\":\"deleted attendance status\"}");
    }

    @PutMapping
    public ResponseEntity<String> updateAttendanceStatus(@RequestBody AttendanceStatus attendanceStatus,
                                                         HttpSession session) {
        Integer userTypeId = (Integer) session.getAttribute("user_type_id");
        if (userTypeId != 0)
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can update attendance statuses\"}");

        try {
            if (attendanceStatusRepo.updateAttendanceStatus(attendanceStatus)==0)
                return ResponseEntity.badRequest().body("{\"message\":\"couldn't update attendance status\"}");
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"couldn't update attendance status\"}");
        }
        return ResponseEntity.ok("{\"message\":\"updated attendance status\"}");
    }

    @GetMapping
    public List<AttendanceStatus> getAllAttendanceStatuses() {
        return attendanceStatusRepo.findAllAttendanceStatuses();
    }
}
