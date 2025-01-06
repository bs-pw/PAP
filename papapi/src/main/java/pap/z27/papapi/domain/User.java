package pap.z27.papapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.relational.core.sql.In;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor

@NoArgsConstructor
public class User implements Serializable {
    private Integer user_id;
    private String name;
    private String surname;
    private Integer user_type_id;
    private String password;
    private String mail;
    public User(String name, String surname, String password, String mail, Integer user_type_id) {
        this.user_id = null;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.mail = mail;
        this.user_type_id = user_type_id;
    }
}