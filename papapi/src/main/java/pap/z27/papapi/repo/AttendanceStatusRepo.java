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
    public List<AttendanceStatus> findAllAttendanceStatuss() {
        return jdbcClient.sql("SELECT * from ATTENCANCE_STATUSES")
                .query(AttendanceStatus.class)
                .list();
    }
    public Integer insertAttendanceStatus(AttendanceStatus AttendanceStatus) {
        return jdbcClient.sql("INSERT INTO ATTENCANCE_STATUSES (ATTENCANCE_STATUSES_ID, status) VALUES (?,?)")
                .param(AttendanceStatus.getAttendance_status_id())
                .param(AttendanceStatus.getStatus())
                .update();
    }
    public Integer updateAttendanceStatus(AttendanceStatus AttendanceStatus){
        return jdbcClient.sql("UPDATE ATTENCANCE_STATUSES set status=? where ATTENCANCE_STATUSES_ID=?")
                .param(AttendanceStatus.getStatus())
                .param(AttendanceStatus.getAttendance_status_id())
                .update();
    }
    public Integer removeAttendanceStatus(AttendanceStatus AttendanceStatus){
        return jdbcClient.sql("DELETE FROM ATTENCANCE_STATUSES where ATTENCANCE_STATUSES_ID=? and status=?")
                .param(AttendanceStatus.getAttendance_status_id())
                .param(AttendanceStatus.getStatus())
                .update();
    }
}

