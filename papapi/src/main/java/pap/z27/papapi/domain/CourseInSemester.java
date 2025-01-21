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
public class CourseInSemester implements Serializable {
    private String course_code;
    private String semester;
    private Boolean is_closed;
    public CourseInSemester(String course_code, String semester) {
        this.course_code = course_code;
        this.semester = semester;
        this.is_closed = false;
    }
}
