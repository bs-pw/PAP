package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.Attendance;

import java.util.List;

@Repository
public class AttendanceRepo {

    @Autowired
    private final JdbcClient jdbcClient;
    public AttendanceRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    public List<Attendance> findAllUsersAttendances(Integer userID) {
        return jdbcClient.sql("SELECT * from ATTENDANCE WHERE user_id=?")
                .param(userID)
                .query(Attendance.class)
                .list();
    }

    public Integer insertAttendance(Attendance attendance) {
        return jdbcClient.sql("INSERT INTO ATTENDANCE (course_code,semester,group_number,attendance_status,"+
                "classes_hour,classes_day,who_inserted_id,user_id) VALUES (?,?,?,?,?,?,?,?)")
                .param(attendance.getCourse_code())
                .param(attendance.getSemester())
                .param(attendance.getGroup_number())
                .param(attendance.getAttendance_status())
                .param(attendance.getHour())
                .param(attendance.getDay())
                .param(attendance.getWho_inserted_id())
                .param(attendance.getUser_id())
                .update();
    }
    public Integer changeAttendance(Attendance attendance) {
        return jdbcClient.sql("UPDATE ATTENDANCE set attendance_status=?,who_inserted_id=? where course_code=?,semester=?,group_number=?,"+
                        "classes_hour=?,classes_day=?,user_id=?")
                .param(attendance.getAttendance_status())
                .param(attendance.getWho_inserted_id())
                .param(attendance.getCourse_code())
                .param(attendance.getSemester())
                .param(attendance.getGroup_number())
                .param(attendance.getHour())
                .param(attendance.getDay())
                .param(attendance.getUser_id())
                .update();
    }

}
