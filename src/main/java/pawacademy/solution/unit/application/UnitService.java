package pawacademy.solution.unit.application;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pawacademy.ResponseException;
import pawacademy.solution.lesson.application.dto.LessonDto;
import pawacademy.solution.lesson.domain.CompletedLesson;
import pawacademy.solution.lesson.domain.CompletedLessonRepository;
import pawacademy.solution.lesson.domain.Lesson;
import pawacademy.solution.lesson.domain.LessonRepository;
import pawacademy.solution.question.application.dto.QuestionDto;
import pawacademy.solution.question.domain.Question;
import pawacademy.solution.unit.application.dto.ExamAnswerDto;
import pawacademy.solution.unit.application.dto.UnitDto;
import pawacademy.solution.unit.domain.ExamTrial;
import pawacademy.solution.unit.domain.ExamTrialRepository;
import pawacademy.solution.unit.domain.Unit;
import pawacademy.solution.unit.domain.UnitRepository;
import pawacademy.solution.user.application.authentication.CurrentUser;
import pawacademy.solution.user.domain.User;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnitService {
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    ExamTrialRepository examTrialRepository;
    @Autowired
    CompletedLessonRepository completedLessonRepository;
    ModelMapper mapper = new ModelMapper();

    public UnitService() {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Transactional
    public String submitExam(ExamAnswerDto examAnswerDto, Long unitId, @CurrentUser User user) throws ResponseException {
        var unit = unitRepository.findById(unitId).orElseThrow(() -> new ResponseException("Unit not found"));
        List<QuestionDto> questions = getUnitExam(unitId, true);

        if (questions.size() != examAnswerDto.getAnswers().size()) {
            throw new ResponseException("Exam questions are incomplete or duplicated");
        }

        int correctAnswers = getCorrectAnswers(examAnswerDto, questions);

        double successRate = ((double) correctAnswers) / questions.size() * 100;
        createExamTrial(user, unit, successRate);

        return "Exam submitted successfully. Success rate: " + successRate + "%";
    }

    private void createExamTrial(User user, Unit unit, Double score) {
        ExamTrial examTrial = new ExamTrial();
        examTrial.setUser(user);
        examTrial.setUnit(unit);
        examTrial.setScore(score);
        examTrialRepository.save(examTrial);

    }

    private static int getCorrectAnswers(ExamAnswerDto examAnswerDto, List<QuestionDto> questions) throws ResponseException {
        int correctAnswers = 0;
        for (int i = 0; i < questions.size(); i++) {
            QuestionDto question = questions.get(i);
            if (question.getId().equals(examAnswerDto.getAnswers().get(i).getQuestionId())) {
                if (question.getCorrectAnswer().getId().equals(examAnswerDto.getAnswers().get(i).getOptionId())) {
                    correctAnswers++;
                }
            } else {
                throw new ResponseException("Exam questions are incomplete or duplicated");
            }
        }
        return correctAnswers;
    }

    public List<QuestionDto> getUnitExam(long unitId, boolean includeAnswers) throws ResponseException {
        var unit = unitRepository.findById(unitId).orElseThrow(() -> new ResponseException("Unit not found"));
        List<QuestionDto> result = new ArrayList<>();
        for (Question question : unit.getExamQuestions()) {
            if (!includeAnswers) {
                question.setCorrectAnswer(null);

            }
            result.add(mapper.map(question, QuestionDto.class));
        }

        return result;
    }

    public List<UnitDto> getUnits(@CurrentUser User user) {
        List<Object[]> results = unitRepository.findWithCompletedLessonsAndTotalLessonsByUserId(user.getId());

        return results.stream()
                .map(this::convertToUnitDto)
                .collect(Collectors.toList());
    }

    private UnitDto convertToUnitDto(Object[] result) {
        UnitDto unitDto = new UnitDto();
        unitDto.setId(((BigInteger) result[0]).longValue());
        unitDto.setName((String) result[1]);
        unitDto.setDescription((String) result[2]);
        unitDto.setImage((String) result[3]);
        unitDto.setCompletedLessons(((Number) result[5]).intValue());
        unitDto.setTotalLessons(((Number) result[6]).intValue());
        return unitDto;
    }

    @Transactional
    public List<LessonDto> getLessons(long unitId, @CurrentUser User user) {
        List<LessonDto> result = new ArrayList<>();
        for (var lesson : getLessonsByUnitId(unitId)) {
            boolean passed = isLessonPassedByUser(lesson, user);

            var lessonDto = mapper.map(lesson, LessonDto.class);
            lessonDto.setPassed(passed);
            result.add(lessonDto);
        }

        return result;
    }

    @Transactional
    public List<Lesson> getLessonsByUnitId(long unitId) {
        var unit = unitRepository.findById(unitId).orElseThrow();
        return unit.getLessons();
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

