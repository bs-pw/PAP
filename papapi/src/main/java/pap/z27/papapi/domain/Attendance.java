package pap.z27.papapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    private Integer user_id;
    private String course_code;
    private String semester;
    private Integer group_number;
    private Integer class_id_for_group;
    private Date date;
    private String attendance_status;
    private Integer who_inserted_id;
}
