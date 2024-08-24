package pawacademy.solution.unit.application;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawacademy.solution.lesson.application.dto.LessonDto;
import pawacademy.solution.lesson.domain.CompletedLesson;
import pawacademy.solution.lesson.domain.CompletedLessonRepository;
import pawacademy.solution.lesson.domain.Lesson;
import pawacademy.solution.lesson.domain.LessonRepository;
import pawacademy.solution.unit.application.dto.UnitDto;
import pawacademy.solution.unit.domain.Unit;
import pawacademy.solution.unit.domain.UnitRepository;
import pawacademy.solution.user.application.authentication.CurrentUser;
import pawacademy.solution.user.domain.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UnitService {
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    CompletedLessonRepository completedLessonRepository;

    public List<UnitDto> getUnits(@CurrentUser User user) {
        List<UnitDto> result = new ArrayList<>();
        for (Unit unit : unitRepository.findAll()) {
            UnitDto unitDto = new UnitDto();
            ModelMapper mapper = new ModelMapper();
            mapper.map(unit, unitDto);

            result.add(unitDto);
        }
        return result;
    }

    @Transactional
    public List<LessonDto> getLessons(long unitId, @CurrentUser User user) {
        var unit = unitRepository.findById(unitId).orElseThrow();
        ModelMapper mapper = new ModelMapper();
        // Define property maps for nested objects
        List<LessonDto> result = new ArrayList<>();
        for (var lesson : unit.getLessons()) {
            if (isLessonPassedByUser(lesson, user)) {
                lesson.setPassed(true);
            }
            result.add(mapper.map(lesson, LessonDto.class));
        }

        return result;
    }

    public CompletedLesson markLessonAsCompleted(User user, Long unitId, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();

        return completedLessonRepository.findByLessonIdAndUserId(lessonId, user.getId())
                .orElseGet(() -> {
                    CompletedLesson newCompletion = new CompletedLesson();
                    newCompletion.setUser(user);
                    newCompletion.setLesson(lesson);
                    return completedLessonRepository.save(newCompletion);
                });
    }

    private boolean isLessonPassedByUser(Lesson lesson, User user) {
        return completedLessonRepository.findByLessonIdAndUserId(lesson.getId(), user.getId()).isPresent();
    }
}

