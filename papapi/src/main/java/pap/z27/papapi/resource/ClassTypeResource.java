package pap.z27.papapi.resource;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pap.z27.papapi.domain.ClassType;
import pap.z27.papapi.domain.UserType;
import pap.z27.papapi.repo.ClassTypeRepo;

import java.util.List;

@RestController
@RequestMapping("/api/classtype")
public class ClassTypeResource {private final ClassTypeRepo classTypeRepo;
    @Autowired
    public ClassTypeResource(ClassTypeRepo classTypeRepo){this.classTypeRepo=classTypeRepo;}
    @GetMapping
    public List<ClassType> getAllClassTypes()
    {
        return classTypeRepo.findAllClassTypes();
    }
    @PostMapping
    public ResponseEntity<String> insertClassType (@RequestBody ClassType classType, HttpSession session)
    {
        Integer classTypeId = (Integer)session.getAttribute("user_type_id");
        if (classTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can insert class types\"}\"");
        }
        if(classTypeRepo.insertClassType(classType)==0)
            return ResponseEntity.badRequest().body("{\"message\":\"Couldn't insert class type\"}\"");
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
    @PutMapping
    public ResponseEntity<String> updateClassType (@RequestBody ClassType classType, HttpSession session)
    {
        Integer classTypeId = (Integer)session.getAttribute("user_type_id");
        if (classTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can update class types\"}\"");
        }
        if(classTypeRepo.updateClassType(classType)==0)
            return ResponseEntity.badRequest().body("{\"message\":\"Couldn't update class type\"}\"");
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
    @DeleteMapping
    public ResponseEntity<String> removeClassType (@RequestParam Integer ClassTypeId, HttpSession session)
    {
        Integer classTypeId = (Integer)session.getAttribute("user_type_id");
        if (classTypeId != 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"only admin can remove class types\"}\"");
        }
        if(classTypeRepo.removeClassType(ClassTypeId)==0)
            return ResponseEntity.badRequest().body("{\"message\":\"Couldn't remove class type\"}\"");
        return ResponseEntity.ok("{\"message\":\"ok\"}");
    }
}
