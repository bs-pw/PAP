package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.ClassType;
import pap.z27.papapi.domain.UserType;

import java.util.List;

@Repository
public class UserTypeRepo {
    @Autowired
    private final JdbcClient jdbcClient;

    public UserTypeRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    public List<UserType> findAllUserTypes() {
        return jdbcClient.sql("SELECT * from USER_TYPES")
                .query(UserType.class)
                .list();
    }
    public Integer insertUserType(UserType userType) {
        return jdbcClient.sql("INSERT INTO USER_TYPES (USER_TYPE_ID, type) VALUES (?,?)")
                .param(userType.getUser_type_id())
                .param(userType.getType())
                .update();
    }
    public Integer updateUserType(UserType userType){
        return jdbcClient.sql("UPDATE USER_TYPES set type=? where USER_TYPE_ID=?")
                .param(userType.getType())
                .param(userType.getUser_type_id())
                .update();
    }
    public Integer removeUserType(Integer userTypeId){
        return jdbcClient.sql("DELETE FROM USER_TYPES where USER_TYPE_ID=?")
                .param(userTypeId)
                .update();
    }
}

