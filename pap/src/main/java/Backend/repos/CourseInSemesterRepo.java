package Backend.repos;


import Backend.domains.CourseInSemester;
import Backend.domains.subclasses.CourseInSemesterID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseInSemesterRepo extends JpaRepository<CourseInSemester, CourseInSemesterID> {
}