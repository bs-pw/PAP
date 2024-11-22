package Backend.domains.subclasses;

import Backend.domains.Course;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CourseInSemesterID{
    @ManyToOne
    @JoinColumn(name="courseCode", nullable = false)
    private Course course;
    @Column(nullable = false)
    private String semester;
}
