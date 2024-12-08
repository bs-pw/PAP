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
    private LocalDate date;
//    PreparedStatement st = ...
//    java.util.Date d = ...
//            st.setDate(1, new java.sql.Date(d.getTime()));
    private String description;
    Grade(Integer category_id, String course_code, String semester, Double grade, LocalDate date) {
        this.category_id = category_id;
        this.course_code = course_code;
        this.semester = semester;
        this.grade = grade;
        this.date = date;
        this.description = "";
    }
    Grade(Integer category_id, String course_code, String semester, Double grade) {
        this.category_id = category_id;
        this.course_code = course_code;
        this.semester = semester;
        this.grade = grade;
        this.date = LocalDate.now();
        this.description = "";
    }

}
