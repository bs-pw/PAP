package pap.z27.papapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domains.User;
import pap.z27.papapi.domains.subclasses.Password;
import pap.z27.papapi.domains.subclasses.UserPublicInfo;

import java.util.List;

@Repository
public class PAPRepo {
    @Autowired
    private final JdbcClient jdbcClient;
    public PAPRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<User> findAllUsers() {
        return jdbcClient.sql("SELECT * FROM USERS").query(User.class).list();
    }
    public String findPasswordByMail(String mail) {
        return jdbcClient.sql("SELECT password FROM USERS where mail=?")
                .param(mail)
                .query(Password.class)
                .single()
                .getPassword();
    }
    public UserPublicInfo findUsersInfoByID(Integer userID) {
        return jdbcClient.sql("SELECT user_id, name, surname, mail FROM USERS where user_id=?")
                .param(userID)
                .query(UserPublicInfo.class)
                .single();
    }
    public Integer insertUser(User user) {
        return jdbcClient.sql("INSERT INTO USERS (name, surname, password, mail)VALUES(?,?,?,?)")
                .param(user.getName())
                .param(user.getSurname())
                .param(user.getPassword())
                .param(user.getMail())
                .update();
    }

//    public List<Attendance> findAllAttendances() {
//        return jdbcClient.sql("SELECT * FROM USERS").query(User.class).list();
//    }


}
