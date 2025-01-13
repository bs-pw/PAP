package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.MyClass;
import pap.z27.papapi.domain.subclasses.ClassDTO;

import java.util.List;

@Repository
public class MyClassRepo {
    @Autowired
    private final JdbcClient jdbcClient;
    public MyClassRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    public List<ClassDTO> findAllUsersClasses(Integer userID) {
        return jdbcClient.sql("SELECT c.course_code,c.semester,c.group_number,c.class_id_for_group,ct.type,c.day,c.hour,c.length,c.\"where\" FROM CLASSES c JOIN (SELECT * FROM STUDENTS_IN_GROUPS UNION SELECT * " +
                        "FROM LECTURERS ) uic ON (c.GROUP_NUMBER = uic.GROUP_NUMBER and c.SEMESTER = uic.SEMESTER and c.COURSE_CODE = uic.COURSE_CODE ) join CLASS_TYPES ct using (class_type_id)  WHERE uic.user_id = ?")
                .param(userID)
                .query(ClassDTO.class)
                .list();
    }

    public List<ClassDTO> findAllLecturersClasses(Integer userID) {
        return jdbcClient.sql("SELECT c.course_code,c.semester,c.group_number,c.class_id_for_group,ct.type,c.day,c.hour,c.length,c.\"where\" " +
                        "FROM CLASSES c JOIN (SELECT * FROM LECTURERS ) uic ON (c.GROUP_NUMBER = uic.GROUP_NUMBER and c.SEMESTER = uic.SEMESTER and c.COURSE_CODE = uic.COURSE_CODE ) join CLASS_TYPES ct using (class_type_id) " +
                        "WHERE uic.user_id = ?")
                .param(userID)
                .query(ClassDTO.class)
                .list();
    }
    public Integer insertClass(MyClass myClass) {
        return jdbcClient.sql("INSERT INTO CLASSES (course_code,semester,group_number,class_type_id,day,hour,length,\"where\") VALUES (?,?,?,?,?,?,?,?)")
                .param(myClass.getCourse_code())
                .param(myClass.getSemester())
                .param(myClass.getGroup_number())
                .param(myClass.getClass_type_id())
                .param(myClass.getDay())
                .param(myClass.getHour())
                .param(myClass.getLength())
                .param(myClass.getWhere())
                .update();
    }
    public Integer removeClass(MyClass myClass) {
        return jdbcClient.sql("DELETE FROM CLASSES where course_code=? and semester=? and group_number=? and CLASS_ID_FOR_GROUP=?")
                .param(myClass.getCourse_code())
                .param(myClass.getSemester())
                .param(myClass.getGroup_number())
                .param(myClass.getClass_id_for_group())
                .update();
    }
}
