package pap.z27.papapi.domain.subclasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserPublicInfo {
    private Integer user_id;
    private String name;
    private String surname;
    //    @Enumerated(EnumType.STRING)
    //    private String status;
    private String mail;
}
