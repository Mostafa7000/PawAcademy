package pawacademy.solution.unit.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pawacademy.solution.lesson.application.dto.LessonDto;
import pawacademy.solution.unit.application.dto.UnitDto;
import pawacademy.solution.user.application.authentication.CurrentUser;
import pawacademy.solution.user.domain.User;

import java.util.List;

@RestController
@RequestMapping("units")
public class UnitController {
    @Autowired
    UnitService unitService;

    @GetMapping
    public List<UnitDto> getAll(@CurrentUser User user) {
        return unitService.getUnits(user);
    }

    @GetMapping("/{id}/lessons")
    public List<LessonDto> getLessons(@CurrentUser User user, @PathVariable Long id) {
        return unitService.getLessons(id);
    }
}
