package pap.z27.papapi.domain.subclasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginInfo {
    private Integer user_id;
    private String name;
    private String surname;
    private Integer user_type_id;
    private String mail;
}