package pap.z27.papapi.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pap.z27.papapi.repo.CourseInSemesterRepo;

@RestController
@RequestMapping("/api/studentsingroups")
public class StudentsInGroupsResource {
//    private StudentsInGroupsRepo courseInSemesterRepo;
//
//    @Autowired
//    public StudentsInGroupsResource(CourseInSemesterRepo courseInSemesterRepo) {
//        this.courseInSemesterRepo = courseInSemesterRepo;
//    }
//
//    @PostMapping
//    public ResponseEntity<String> addStudentToGroup() {}
}
