package pap.z27.papapi;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReactController {

    @GetMapping(value = {"/{path:[^\\.]*}", "/", ""})
    public String redirect() {
        return "forward:/index.html";
    }
}

