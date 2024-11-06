package cufe.example.BackEndSection;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "SearchEngineFront-end.html";
    }
    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
