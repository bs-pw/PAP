package pap.z27.papapi.domain.subclasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GradeDTO {
    private Integer category_id;
    private String category_description;
    private String course_code;
    private String semester;
    private Integer user_id;
    private String user_name;
    private String user_surname;
    private Integer who_inserted_id;
    private String who_inserted_name;
    private String who_inserted_surname;
    private Double grade;
    private Timestamp date;
}
