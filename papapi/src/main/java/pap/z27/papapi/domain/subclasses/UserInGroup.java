package pap.z27.papapi.domain.subclasses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInGroup {
    private Integer user_id;
    private String course_code;
    private String semester;
    private Integer group_number;
}
