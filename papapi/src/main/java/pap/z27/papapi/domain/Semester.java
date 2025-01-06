package pap.z27.papapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class Semester {
    private String semester_code;
    private LocalDate start_date;
    private LocalDate end_date;
}
