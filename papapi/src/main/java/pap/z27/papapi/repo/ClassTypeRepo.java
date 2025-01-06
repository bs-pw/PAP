package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.ClassType;

import java.util.List;
@Repository
public class ClassTypeRepo {
    @Autowired
    private final JdbcClient jdbcClient;

    public ClassTypeRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    public List<ClassType> findAllClassTypes() {
        return jdbcClient.sql("SELECT * from CLASS_TYPES")
                .query(ClassType.class)
                .list();
    }
    public Integer insertClassType(ClassType classType) {
        return jdbcClient.sql("INSERT INTO CLASS_TYPES (class_type_id, type) VALUES (?,?)")
                .param(classType.getClass_type_id())
                .param(classType.getType())
                .update();
    }
    public Integer updateClassType(ClassType classType){
        return jdbcClient.sql("UPDATE CLASS_TYPES set type=? where class_type_id=?")
                .param(classType.getType())
                .param(classType.getClass_type_id())
                .update();
    }
    public Integer removeClassType(ClassType classType){
        return jdbcClient.sql("DELETE FROM CLASS_TYPES where class_type_id=? and type=?")
                .param(classType.getClass_type_id())
                .param(classType.getType())
                .update();
    }
}
