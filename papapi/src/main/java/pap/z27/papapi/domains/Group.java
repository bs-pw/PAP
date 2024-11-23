package pap.z27.papapi.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@AllArgsConstructor
public class Group implements Serializable {
    private String course_code;
    private String semester;
    private Integer group_number;

}
