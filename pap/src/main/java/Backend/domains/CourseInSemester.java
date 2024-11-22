package Backend.domains;

import Backend.domains.subclasses.CourseInSemesterID;
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
@Table(name="COURSES_IN_SEMESTER")
public class CourseInSemester {
    @EmbeddedId
    private CourseInSemesterID cisID;
    @ManyToMany(mappedBy = "isCoordinatorOf")
    List<User> Coordinators;
    @OneToMany(mappedBy = "groupID.course")
    List<Group> groups;


}
