package pap.z27.papapi.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
public class Course implements Serializable {
    private String course_code;
    private String title;
}
