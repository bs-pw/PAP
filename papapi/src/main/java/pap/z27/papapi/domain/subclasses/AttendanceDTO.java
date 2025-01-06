package pap.z27.papapi.domain.subclasses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {
    private Integer user_id;
    private String course_code;
    private String semester;
    private Integer group_number;
    private Integer class_id_for_group;
    private LocalDate date;
    private String status;
    private Integer who_inserted_id;
}
