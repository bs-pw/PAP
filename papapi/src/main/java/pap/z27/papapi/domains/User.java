package pap.z27.papapi.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
public class User implements Serializable {
    private Integer user_id;
    private String name;
    private String surname;
//    @Enumerated(EnumType.STRING)
//    private String status;
    private String password;
    private String mail;
    User(String name, String surname, String password, String mail) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.mail = mail;
    }
}