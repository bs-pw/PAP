package Backend;

import Backend.domains.Course;
import Backend.domains.CourseInSemester;
import Backend.domains.Group;
import Backend.domains.User;
import Backend.domains.subclasses.CourseInSemesterID;
import Backend.domains.subclasses.GroupID;
import Backend.repos.CourseInSemesterRepo;
import Backend.repos.CourseRepo;
import Backend.repos.GroupRepo;
import Backend.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class Services {
        @Autowired
        private CourseRepo courseRepo;
        @Autowired
        private UserRepo userRepo;
        @Autowired
        private GroupRepo groupRepo;
        @Autowired
        private CourseInSemesterRepo courseInSemesterRepo;

        public List<User> getUsers() {
            return userRepo.findAll();
        }
        public List<Group> getGroups() {
            return groupRepo.findAll();
        }
        public List<CourseInSemester> getCoursesInSemester() {
            return courseInSemesterRepo.findAll();
        }
        public List<Course> getCourses() {
            return courseRepo.findAll();
        }

        public void createUser(User user) {
            userRepo.save(user);
        }
        public void createGroup( Group group) {
            groupRepo.save(group);
        }
        public void createCourse(Course course) {
            courseRepo.save(course);
        }

        public void addCourseInSemester(String courseCode, String semester) {
            // Fetch the Course entity from the database
            Course course = courseRepo.findById(courseCode)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            // Create CourseInSemesterID
            CourseInSemesterID cisID = new CourseInSemesterID(course, semester);

            // Create CourseInSemester
            CourseInSemester courseInSemester = new CourseInSemester();
            courseInSemester.setCisID(cisID);
            courseInSemesterRepo.save(courseInSemester);

            course.getCourseInSemesterSet().add(courseInSemester);
            courseRepo.save(course);

        }
        public void addGroup(Integer groupNr, String courseCode, String courseSemester) {
            // Fetch the Course entity from the database
            Course course = courseRepo.findById(courseCode)
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            // Create CourseInSemesterID
            CourseInSemesterID cisID = new CourseInSemesterID(course, courseSemester);
            CourseInSemester courseInSemester = courseInSemesterRepo.findById(cisID)
                    .orElseThrow(() -> new RuntimeException("Course in semester not found"));
            GroupID groupID = new GroupID(groupNr, courseInSemester);
            Group group = new Group();
            group.setGroupID(groupID);
            groupRepo.save(group);
            courseInSemester.getGroups().add(group);
            courseInSemesterRepo.save(courseInSemester);
        }

}
