package pap.z27.papapi.domain.subclasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class NameGrade implements Serializable {
    private String name;
    private Integer grade;
}
