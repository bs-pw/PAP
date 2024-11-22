package Backend;

import Backend.domains.Course;
import Backend.domains.CourseInSemester;
import Backend.domains.Group;
import Backend.domains.User;
import Backend.domains.subclasses.GroupDTO;
import Backend.repos.CourseInSemesterRepo;
import Backend.repos.CourseRepo;
import Backend.repos.GroupRepo;
import Backend.repos.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.zip.CheckedOutputStream;

@RestController
@RequestMapping("/api/r")
@AllArgsConstructor
public class Controller {
    @Autowired
    private final Services services;

    @GetMapping("/u/g")
    List<User> getUsers() {
        return services.getUsers();
    }

    @GetMapping("/cis/g")
    List<CourseInSemester> getCoursesInSemester() {
        return services.getCoursesInSemester();
    }
    @GetMapping("/c/g")
    List<Course> getCourses() {
        return services.getCourses();
    }
    @GetMapping("/g/g")
    List<Group> getGroups() {
        return services.getGroups();
    }

    @PostMapping("/u")
    void createUser(@RequestBody User user) {
        services.createUser(user);
    }
    @PostMapping("/g")
    void createGroup(@RequestBody GroupDTO group) {
        services.addGroup(group.groupNr, group.courseCode, group.courseSemester);
    }
    @PostMapping("/c")
    void createCourse(@RequestBody Course course) {
        services.createCourse(course);
    }
    @PostMapping("/cis")
    public void createCourseInSemester(
            @RequestBody HashMap<String, String> data) {
        String courseCode = data.get("courseCode");
        String semester = data.get("semester");

        services.addCourseInSemester(courseCode, semester);
    }



}
