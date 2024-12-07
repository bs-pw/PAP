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
}
