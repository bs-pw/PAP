package pap.z27.papapi.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
public class Class implements Serializable {
    private String course_code;
    private String semester;
    private Integer group_number;
    private String type;
    private char day;
    private Integer hour;
    private Integer length;
    private String where;
}