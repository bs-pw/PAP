package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.AttendanceStatus;

import java.util.List;

@Repository
public class AttendanceStatusRepo {
    @Autowired
    private final JdbcClient jdbcClient;

    public AttendanceStatusRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    public List<AttendanceStatus> findAllAttendanceStatuses() {
        return jdbcClient.sql("SELECT * from ATTENDANCE_STATUSES")
                .query(AttendanceStatus.class)
                .list();
    }
    public Integer insertAttendanceStatus(AttendanceStatus attendanceStatus) {
        return jdbcClient.sql("INSERT INTO ATTENDANCE_STATUSES (ATTENDANCE_STATUS_ID, status) VALUES (?,?)")
                .param(attendanceStatus.getAttendance_status_id())
                .param(attendanceStatus.getStatus())
                .update();
    }
    public Integer updateAttendanceStatus(AttendanceStatus attendanceStatus){
        return jdbcClient.sql("UPDATE ATTENDANCE_STATUSES set status=? where ATTENDANCE_STATUS_ID=?")
                .param(attendanceStatus.getStatus())
                .param(attendanceStatus.getAttendance_status_id())
                .update();
    }
    public Integer removeAttendanceStatus(Integer attendanceStatusId){
        return jdbcClient.sql("DELETE FROM ATTENDANCE_STATUSES where ATTENDANCE_STATUS_ID=?")
                .param(attendanceStatusId)
                .update();
    }
}

