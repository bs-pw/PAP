package pap.z27.papapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lecturer {
    private Integer userId;
    private String courseCode;
    private String semester;
    private Integer groupNumber;
}
