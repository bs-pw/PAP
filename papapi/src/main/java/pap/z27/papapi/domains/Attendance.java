package pap.z27.papapi.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Attendance {
    private String course_code;
    private String semester;
    private Integer group_number;
    private String type;
    private char day;
    private Integer hour;
    private String attendance_status;
    private Integer user_id;
    private Integer who_inserted_id;
    Attendance(String course_code, String semester, Integer group_number, String type, char day, Integer hour,
               Integer user_id, Integer who_inserted_id) {}
}
