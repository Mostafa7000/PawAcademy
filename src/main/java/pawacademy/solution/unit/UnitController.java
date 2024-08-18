package pawacademy.solution.unit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnitController {

    @GetMapping("success")
    public String success() {
        return "Success!";
    }
}
