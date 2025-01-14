package pap.z27.papapi.domain.subclasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAndFinalGrade implements Serializable {
    private Integer user_id;
    private String name;
    private String surname;
    private Double grade;
}
