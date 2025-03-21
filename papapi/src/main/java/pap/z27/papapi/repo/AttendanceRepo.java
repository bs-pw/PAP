package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.Attendance;
import pap.z27.papapi.domain.subclasses.AttendanceDTO;

import java.util.List;

@Repository
public class AttendanceRepo {

    @Autowired
    private final JdbcClient jdbcClient;
    public AttendanceRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    public List<AttendanceDTO> findAllAttendances() {
        return jdbcClient.sql("SELECT a.user_id,a.course_code,a.semester,a.group_number,a.class_id_for_group,a.\"date\",ats.status, a.who_inserted_id from ATTENDANCES a inner join ATTENDANCE_STATUSES ats on a.attendance_status_id=ats.attendance_status_id")
                .query(AttendanceDTO.class)
                .list();
    }

    public List<AttendanceDTO> findAllStudentsAttendances(Integer userID) {
        return jdbcClient.sql("SELECT a.user_id,a.course_code,a.semester,a.group_number,a.class_id_for_group,a.\"date\",ats.status, a.who_inserted_id from ATTENDANCES a inner join ATTENDANCE_STATUSES ats on a.attendance_status_id=ats.attendance_status_id WHERE a.user_id=?")
                .param(userID)
                .query(AttendanceDTO.class)
                .list();
    }

    public List<AttendanceDTO> findAllStudentsAttendancesInCourse(Integer userID, String courseCode, String semester) {
        return jdbcClient.sql("SELECT a.user_id,a.course_code,a.semester,a.group_number,a.class_id_for_group,a.\"date\",ats.status, a.who_inserted_id from ATTENDANCES a inner join ATTENDANCE_STATUSES ats on a.attendance_status_id=ats.attendance_status_id WHERE a.user_id=? and a.course_code=? and a.semester=?")
                .param(userID)
                .param(courseCode)
                .param(semester)
                .query(AttendanceDTO.class)
                .list();
    }

    public List<AttendanceDTO> findAllAttendancesInGroup(String courseCode, String semester, Integer groupNr) {
        return jdbcClient.sql("SELECT a.user_id,a.course_code,a.semester,a.group_number,a.class_id_for_group,a.\"date\",ats.status, a.who_inserted_id from ATTENDANCES a inner join ATTENDANCE_STATUSES ats on a.attendance_status_id=ats.attendance_status_id WHERE a.course_code=? and a.semester=? and a.group_number=?")
                .param(courseCode)
                .param(semester)
                .param(groupNr)
                .query(AttendanceDTO.class)
                .list();
    }

    public Integer insertAttendance(Attendance attendance) {
        return jdbcClient.sql("INSERT INTO ATTENDANCES (course_code,semester,group_number,attendance_status_id,"+
                "class_id_for_group,\"date\",who_inserted_id,user_id) VALUES (?,?,?,?,?,?,?,?)")
                .param(attendance.getCourse_code())
                .param(attendance.getSemester())
                .param(attendance.getGroup_number())
                .param(attendance.getAttendance_status_id())
                .param(attendance.getClass_id_for_group())
                .param(attendance.getDate())
                .param(attendance.getWho_inserted_id())
                .param(attendance.getUser_id())
                .update();
    }
    public Integer updateAttendance(Attendance attendance) {
        return jdbcClient.sql("UPDATE ATTENDANCES set attendance_status_id=?,who_inserted_id=? where course_code=? and semester=? and group_number=? and "+
                        "class_id_for_group=? and \"date\"=? and user_id=?")
                .param(attendance.getAttendance_status_id())
                .param(attendance.getWho_inserted_id())
                .param(attendance.getCourse_code())
                .param(attendance.getSemester())
                .param(attendance.getGroup_number())
                .param(attendance.getClass_id_for_group())
                .param(attendance.getDate())
                .param(attendance.getUser_id())
                .update();
    }

}
