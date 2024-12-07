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
public class GradeCategory implements Serializable {
    private Integer category_id;
    private String course_code;
    private String semester;
    private String description;
    private Double wage;
    GradeCategory(Integer category_id, String course_code, String semester, Double wage) {
        this.category_id = category_id;
        this.course_code = course_code;
        this.semester = semester;
        this.wage = wage;
        this.description = "";
    }
}
