package pawacademy.solution.unit.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{unitId}/lessons")
    public List<LessonDto> getLessons(@CurrentUser User user, @PathVariable Long unitId) {
        return unitService.getLessons(unitId, user);
    }
    @PostMapping("/{unitId}/lessons/{lessonId}/complete")
    public ResponseEntity<Void> markLessonAsCompleted(@CurrentUser User user,
                                                      @PathVariable Long unitId,
                                                      @PathVariable Long lessonId) {
        unitService.markLessonAsCompleted(user, unitId, lessonId);
        return ResponseEntity.ok().build(); // 200 OK, or 204 No Content
    }
}
