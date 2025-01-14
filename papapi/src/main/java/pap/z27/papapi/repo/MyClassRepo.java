package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.MyClass;
import pap.z27.papapi.domain.subclasses.ClassDTO;
import pap.z27.papapi.domain.subclasses.ClassInfo;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MyClassRepo {
    @Autowired
    private final JdbcClient jdbcClient;
    public MyClassRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    public List<ClassDTO> findAllClasses() {
        return jdbcClient.sql("SELECT c.course_code,c.semester,c.group_number,c.class_id_for_group,ct.type,c.day,c.hour,c.length,c.\"where\" FROM CLASSES c join CLASS_TYPES ct using (class_type_id)")
                .query(ClassDTO.class)
                .list();
    }
    public List<ClassDTO> findAllUsersClasses(Integer userID) {
        return jdbcClient.sql("SELECT c.course_code,c.semester,c.group_number,c.class_id_for_group,ct.type,c.day,c.hour,c.length,c.\"where\" FROM CLASSES c JOIN (SELECT * FROM STUDENTS_IN_GROUPS UNION SELECT * " +
                        "FROM LECTURERS ) uic ON (c.GROUP_NUMBER = uic.GROUP_NUMBER and c.SEMESTER = uic.SEMESTER and c.COURSE_CODE = uic.COURSE_CODE ) join CLASS_TYPES ct using (class_type_id)  WHERE uic.user_id = ?")
                .param(userID)
                .query(ClassDTO.class)
                .list();
    }
    public List<ClassInfo> findAllClassesInGroup(String semester, String courseCode, Integer groupNr) {
        return jdbcClient.sql("SELECT c.course_code,c.semester,c.group_number,c.class_id_for_group,c.class_type_id,ct.type,c.day,c.hour,c.length,c.\"where\" FROM CLASSES c  join CLASS_TYPES ct using (class_type_id)  WHERE c.SEMESTER = ? and c.COURSE_CODE=? and c.GROUP_NUMBER=?")
                .param(semester)
                .param(courseCode)
                .param(groupNr)
                .query(ClassInfo.class)
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

    public ClassDTO findClass(String courseCode, String semester, Integer groupNr, Integer classIdForGroup){
        return jdbcClient.sql("SELECT c.course_code,c.semester,c.group_number,c.class_id_for_group,ct.type,c.day,c.hour,c.length,c.\"where\" FROM CLASSES c join CLASS_TYPES ct using (class_type_id)  " +
                        "WHERE c.course_code = ? and c.semester=? and c.group_number=? and c.class_id_for_group=?")
                .param(courseCode)
                .param(semester)
                .param(groupNr)
                .param(classIdForGroup)
                .query(ClassDTO.class)
                .single();
    }

    public List<MyClass> findClassPlanBySemester(String courseCode, String semester) {
        return jdbcClient.sql("SELECT c.course_code,c.semester,c.group_number,c.class_id_for_group,c.class_type_id," +
                "c.day,c.hour,c.length,c.\"where\" " +
                "FROM classes c " +
                "WHERE c.course_code = ? AND c.semester = ?")
                .param(courseCode)
                .param(semester)
                .query(MyClass.class)
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
    public Integer updateClass(MyClass myClass) {
        StringBuilder query = new StringBuilder("UPDATE CLASSES set ");
        List<Object> params=new ArrayList<>(){};
        if(myClass.getDay()!=null)
        {
            query.append("day=?, ");
            params.add(myClass.getDay());
        }
        if(myClass.getHour()!=null)
        {
            query.append("hour=?, ");
            params.add(myClass.getHour());
        }
        if(myClass.getLength()!=null)
        {
            query.append("length=?, ");
            params.add(myClass.getLength());
        }
        if(myClass.getWhere()!=null)
        {
            query.append("\"where\"=?, ");
            params.add(myClass.getWhere());
        }
        if(myClass.getClass_type_id()!=null)
        {
            query.append("CLASS_TYPE_ID=?, ");
            params.add(myClass.getClass_type_id());
        }
        if(params.isEmpty()) return 0;
        else {
            query.setLength(query.length() - 2);
            query.append(" WHERE COURSE_CODE=? and SEMESTER=? and group_number=? and class_id_for_group=?");
            params.add(myClass.getCourse_code());
            params.add(myClass.getSemester());
            params.add(myClass.getGroup_number());
            params.add(myClass.getClass_id_for_group());
            return jdbcClient.sql(query.toString())
                    .params(params)
                    .update();
        }
    }
    public Integer removeClass(String courseCode, String semester, Integer groupNr, Integer classIdForGroup) {
        return jdbcClient.sql("DELETE FROM CLASSES where course_code=? and semester=? and group_number=? and CLASS_ID_FOR_GROUP=?")
                .param(courseCode)
                .param(semester)
                .param(groupNr)
                .param(classIdForGroup)
                .update();
    }
}
