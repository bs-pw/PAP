package pap.z27.papapi.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import pap.z27.papapi.domain.CourseInSemester;
import pap.z27.papapi.domain.Group;
import pap.z27.papapi.domain.User;
import pap.z27.papapi.domain.subclasses.UserInGroup;
import pap.z27.papapi.domain.subclasses.UserPublicInfo;

import java.util.List;
@Repository
public class GroupRepo {
    @Autowired
    private final JdbcClient jdbcClient;
    public GroupRepo(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Group> findAllGroups() {
        return jdbcClient.sql("SELECT * from GROUPS")
                .query(Group.class)
                .list();
    }
    public List<Group> findAllGroupsByCourseInSemester(CourseInSemester courseInSemester) {
        return jdbcClient.sql("SELECT * from GROUPS where course_code = ? and semester = ?")
                .param(courseInSemester.getCourse_code())
                .param(courseInSemester.getSemester())
                .query(Group.class)
                .list();
    }
    public List<Group> findAllUsersGroupsThisSemester(Integer user_id, String semester) {
        return jdbcClient.sql("SELECT * from GROUPS g join STUDENTS_IN_GROUPS sic on(sic.course_code=g.course_code and sic.semester=g.semester and sic.group_number=g.group_number) where sic.user_id = ? and g.semester = ?")
                .param(user_id)
                .param(semester)
                .query(Group.class)
                .list();
    }
    public List<Group> findAllUsersGroups(Integer user_id) {
        return jdbcClient.sql("SELECT * from GROUPS g join STUDENTS_IN_GROUPS sic on(sic.course_code=g.course_code and sic.semester=g.semester and sic.group_number=g.group_number) where sic.user_id = ?")
                .param(user_id)
                .query(Group.class)
                .list();
    }
    public List<Group> findAllLecturersGroupsThisSemester(Integer user_id, String semester) {
        return jdbcClient.sql("SELECT * from GROUPS g join LECTURERS l on(l.course_code=g.course_code and l.semester=g.semester and l.group_number=g.group_number) where l.user_id = ? and g.semester = ?")
                .param(user_id)
                .param(semester)
                .query(Group.class)
                .list();
    }
    public List<Group> findAllLecturersGroups(Integer user_id) {
        return jdbcClient.sql("SELECT * from GROUPS g join LECTURERS l on(l.course_code=g.course_code and l.semester=g.semester and l.group_number=g.group_number) where l.user_id = ?")
                .param(user_id)
                .query(Group.class)
                .list();
    }
    public Integer insertGroup(Group group) {
        return jdbcClient.sql("INSERT INTO GROUPS (course_code,semester,group_number) VALUES (?,?,?)")
                .param(group.getCourse_code())
                .param(group.getSemester())
                .param(group.getGroup_number())
                .update();
    }
    public Integer addStudentToGroup(UserInGroup userInGroup) {
        return jdbcClient.sql("INSERT INTO STUDENTS_IN_GROUPS (user_id,course_code,semester,group_number) VALUES (?,?,?,?)")
                .param(userInGroup.getUser_id())
                .param(userInGroup.getCourse_code())
                .param(userInGroup.getSemester())
                .param(userInGroup.getGroup_number())
                .update();
    }

    public Integer removeStudentFromGroup(UserInGroup userInGroup) {
        return jdbcClient.sql("DELETE FROM STUDENTS_IN_GROUPS WHERE user_id=? and course_code=? and semester=? and group_number=?")
                .param(userInGroup.getUser_id())
                .param(userInGroup.getCourse_code())
                .param(userInGroup.getSemester())
                .param(userInGroup.getGroup_number())
                .update();
    }
    public Integer addLecturerToGroup(Integer userID, Group group) {
        return jdbcClient.sql("INSERT INTO LECTURERS (user_id,course_code,semester,group_number) VALUES (?,?,?,?)")
                .param(userID)
                .param(group.getCourse_code())
                .param(group.getSemester())
                .param(group.getGroup_number())
                .update();
    }

    public Integer removeLecturerFromGroup(Integer userID, Group group) {
        return jdbcClient.sql("DELETE FROM LECTURERS WHERE user_id=? and course_code=? and semester=? and group_number=?")
                .param(userID)
                .param(group.getCourse_code())
                .param(group.getSemester())
                .param(group.getGroup_number())
                .update();
    }
//    public Integer countGroups(Group group){
//        return jdbcClient.sql("SELECT count(*) from GROUPS where course_code=? and semester=? and group_number=?")
//                .param(group.getCourse_code())
//                .param(group.getSemester())
//                .param(group.getGroup_number())
//                .query(Integer.class)
//                .single();
//    }
    public Integer removeGroup(Group group){
        return jdbcClient.sql("DELETE FROM GROUPS where course_code=? and semester=? and group_number=?")
                .param(group.getCourse_code())
                .param(group.getSemester())
                .param(group.getGroup_number())
                .update();
    }
    public Integer changeStudentsGroup(UserInGroup userInGroup, Integer newGroupNr)
    {
        return jdbcClient.sql("UPDATE STUDENTS_IN_GROUPS set GROUP_NUMBER=? where course_code=? and semester=? and group_number=? and USER_ID=?")
                .param(userInGroup.getCourse_code())
                .param(userInGroup.getSemester())
                .param(userInGroup.getGroup_number())
                .param(newGroupNr)
                .update();
    }
    public Integer changeLecturersGroup(UserInGroup userInGroup, Integer newGroupNr)
    {
        return jdbcClient.sql("UPDATE LECTURERS set GROUP_NUMBER=? where course_code=? and semester=? and group_number=? and USER_ID=?")
                .param(userInGroup.getCourse_code())
                .param(userInGroup.getSemester())
                .param(userInGroup.getGroup_number())
                .param(newGroupNr)
                .update();
    }


}
