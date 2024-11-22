package Backend.repos;


import Backend.domains.Group;
import Backend.domains.subclasses.GroupID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepo extends JpaRepository<Group, GroupID> {
}