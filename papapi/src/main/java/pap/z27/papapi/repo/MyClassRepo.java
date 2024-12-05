package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.MyClass;

import java.util.List;

@Repository
public class MyClassRepo {
    @Autowired
    private final JdbcClient jdbcClient;
    public MyClassRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    public List<MyClass> findAllUsersClasses(Integer userID) {
        return jdbcClient.sql("SELECT c.* FROM CLASSES c JOIN (SELECT user_id FROM STUDENTS_IN_CLASSES UNION SELECT user_id" +
                        "FROM LECTURERS ) uic ON c.user_id = uic.user_id WHERE c.user_id = ?")
                .param(userID)
                .query(MyClass.class)
                .list();
    }
    public List<MyClass> findAllLecturersClasses(Integer userID) {
        return jdbcClient.sql("SELECT c.* FROM CLASSES c JOIN (SELECT user_id FROM LECTURERS ) uic ON c.user_id = uic.user_id WHERE c.user_id = ?")
                .param(userID)
                .query(MyClass.class)
                .list();
    }
    public Integer insertClass(MyClass myClass) {
        return jdbcClient.sql("INSERT INTO CLASSES (course_code,semester,group_number,type,day,hour,length,'where') VALUES (?,?,?,?,?,?,?,?)")
                .param(myClass.getCourse_code())
                .param(myClass.getSemester())
                .param(myClass.getGroup_number())
                .param(myClass.getType())
                .param(myClass.getDay())
                .param(myClass.getHour())
                .param(myClass.getLength())
                .param(myClass.getWhere())
                .update();
    }
    public Integer removeClass(MyClass myClass) {
        return jdbcClient.sql("DELETE FROM CLASSES where course_code=? and semester=? and group_number=? and type=? and day=? and hour=? and length=? and 'where'=?")
                .param(myClass.getCourse_code())
                .param(myClass.getSemester())
                .param(myClass.getGroup_number())
                .param(myClass.getType())
                .param(myClass.getDay())
                .param(myClass.getHour())
                .param(myClass.getLength())
                .param(myClass.getWhere())
                .update();
    }

}
