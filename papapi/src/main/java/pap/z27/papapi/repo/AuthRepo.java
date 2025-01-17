package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.SecurityConfig;
import pap.z27.papapi.domain.subclasses.Credentials;
import pap.z27.papapi.domain.subclasses.Password;

@Repository
public class AuthRepo {
    @Autowired
    private final JdbcClient jdbcClient;
    private final SecurityConfig securityConfig;
    public AuthRepo(JdbcClient jdbcClient, SecurityConfig securityConfig) {
        this.jdbcClient = jdbcClient;
        this.securityConfig = securityConfig;
    }

    public boolean isPasswordCorrect(Credentials credentials) {
        try{
            Password passwordFromDB = jdbcClient.sql("SELECT password FROM USERS where mail=?")
                .param(credentials.getMail())
                .query(Password.class)
                .optional().orElse(null);
            if(passwordFromDB == null)
                return false;
            return securityConfig.passwordEncoder().matches(credentials.getPassword(), passwordFromDB.getPassword());
        }catch (DataAccessException e){
            return false;
        }
    }
}
