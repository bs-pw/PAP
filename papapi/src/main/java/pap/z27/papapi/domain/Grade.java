package pap.z27.papapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    private Integer category_id;
    private String course_code;
    private String semester;
    private Integer user_id;
    private Integer who_inserted_id;
    private Double grade;

    private String description;
    Grade(Integer category_id, String course_code, String semester, Double grade) {
        this.category_id = category_id;
        this.course_code = course_code;
        this.semester = semester;
        this.grade = grade;
        this.description = "";
    }

}
