package pap.z27.papapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class FinalGrade implements Serializable {
    private Integer user_id;
    private String course_code;
    private String semester;
    private Double grade;
}
