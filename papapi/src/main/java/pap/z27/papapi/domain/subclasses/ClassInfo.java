package pap.z27.papapi.domain.subclasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassInfo {
    private String course_code;
    private String semester;
    private Integer group_number;
    private Integer class_id_for_group;
    private Integer class_type_id;
    private String type;
    private Integer day;
    private Integer hour;
    private Integer length;
    private String where;
}