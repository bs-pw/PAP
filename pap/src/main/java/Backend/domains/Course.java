package Backend.domains;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="COURSES")
public class Course {
    @Id
    @Column(nullable=false)
    private String code;
    @Column(nullable=false)
    private String name;
    @OneToMany(mappedBy = "cisID.course")
    List<CourseInSemester> courseInSemesterSet;
}
