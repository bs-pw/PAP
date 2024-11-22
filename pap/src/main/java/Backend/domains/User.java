package Backend.domains;

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
@Table(name="USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private String surname;
    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false)
    private String mail;
    private String mayor;
    @ManyToMany
    @JoinTable(
            name="Lecturers",
            joinColumns = @JoinColumn(name="userID"),
            inverseJoinColumns = {
                    @JoinColumn(name = "groupID"),
                    @JoinColumn(name = "courseCode"),
                    @JoinColumn(name = "semester")}
    )
    List<Group> isLecturerOf;
    @ManyToMany
    @JoinTable(
            name="Students_In_Groups",
            joinColumns = @JoinColumn(name="userID"),
            inverseJoinColumns = {
                    @JoinColumn(name = "groupID"),
                    @JoinColumn(name = "courseCode"),
                    @JoinColumn(name = "semester")}

    )
    List<Group> isInGroup;

    @ManyToMany
    @JoinTable(
            name="Coordinators",
            joinColumns = @JoinColumn(name="userID"),
            inverseJoinColumns = {
                    @JoinColumn(name = "courseCode"),
                    @JoinColumn(name = "semester")
            }
    )
    List<CourseInSemester> isCoordinatorOf;

    public enum Status
    {
        Student,
        Staff,
        Admin
    }

}