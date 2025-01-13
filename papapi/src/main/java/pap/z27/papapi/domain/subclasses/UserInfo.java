package pap.z27.papapi.domain.subclasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfo {
    private Integer user_id;
    private String name;
    private String surname;
    private Integer user_type_id;
    private String type;
    private String mail;
}