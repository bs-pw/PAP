package Backend.domains;


import Backend.domains.subclasses.GroupID;
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
@Table(name="GROUPS")
public class Group {

    @EmbeddedId
    private GroupID groupID;
    @ManyToMany(mappedBy = "isLecturerOf")
    List<User> Lecturers;
    @ManyToMany(mappedBy = "isInGroup")
    List<User> Students;

}
