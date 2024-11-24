package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.subclasses.Password;

@Repository
public class AuthRepo {
    @Autowired
    private final JdbcClient jdbcClient;
    public AuthRepo(JdbcClient jdbcClient) {this.jdbcClient = jdbcClient;}

    public boolean isPasswordCorrect(String mail, Password password) {
        String passwordFromDB = jdbcClient.sql("SELECT password FROM USERS where mail=?")
                .param(mail)
                .query(Password.class)
                .single()
                .getPassword();
        if (passwordFromDB.equals(password.getPassword())) return true;
        return false;
    }
}
