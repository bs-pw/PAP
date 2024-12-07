package pap.z27.papapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyClass implements Serializable {
    private String course_code;
    private String semester;
    private Integer group_number;
    private Integer class_id_for_group;
    private String type;
    private char day;
    private Integer hour;
    private Integer length;
    private String where;
}