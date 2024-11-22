package Backend.domains.subclasses;

import Backend.domains.CourseInSemester;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupID{
    @Column(nullable = false)
    private Integer GroupNr;
    @ManyToOne
    @JoinColumn(name="courseCode", nullable = false)
    @JoinColumn(name="courseSemester", nullable = false)
    private CourseInSemester course;
}