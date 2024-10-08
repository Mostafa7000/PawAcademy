package pawacademy.solution.lesson.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pawacademy.ResponseException;
import pawacademy.solution.lesson.domain.Lesson;
import pawacademy.solution.lesson.domain.LessonRepository;
import pawacademy.solution.question.domain.Option;
import pawacademy.solution.question.domain.OptionRepository;
import pawacademy.solution.question.domain.Question;
import pawacademy.solution.question.domain.QuestionRepository;
import pawacademy.solution.unit.domain.UnitRepository;

@Controller
@RequestMapping("/admin/lessons")
public class LessonAdminController {

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping
    public String listLessons(Model model) {
        model.addAttribute("lessons", lessonRepository.findAll());
        return "lessons/list";
    }

    @GetMapping("/new")
    public String createLessonForm(Model model, @RequestParam Long unitId) {
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("unitId", unitId);
        return "lessons/create";
    }

    @PostMapping
    @Transactional
    public String saveLesson(@ModelAttribute("lesson") Lesson lesson, @RequestParam Long unitId) {
        lessonRepository.insert(unitId, lesson.getName(), lesson.getContent(), lesson.getVideo());
        var lessonId = lessonRepository.getLastInsertedId();
        return "redirect:/admin/lessons/edit/" + lessonId + "?unitId=" + unitId;
    }

    @GetMapping("/edit/{id}")
    public String editLessonForm(@PathVariable Long id, @RequestParam Long unitId, Model model) {
        var lesson = lessonRepository.findById(id).orElse(new Lesson());
        model.addAttribute("lesson", lesson);
        model.addAttribute("unitId", unitId);

        return "lessons/edit";
    }

    @PostMapping("/update/{id}")
    @Transactional
    public String updateLesson(@PathVariable Long id, @ModelAttribute("lesson") Lesson lesson, @RequestParam Long unitId) throws ResponseException {
        for (Question question : lesson.getNewQuestions()) {
            if (question != null) {
                lesson.getQuestions().add(question);
            }
        }

        for (Question question : lesson.getQuestions()) {
            question.setLesson(lesson);

            for (Option option : question.getNewOptions()) {
                if (option != null) {
                    question.getOptions().add(option);
                }
            }

            for (Option option : question.getOptions()) {
                option.setQuestion(question);
            }

            if (question.getCorrectAnswerId() != null) {
                Option correctAnswer;
                if (question.getCorrectAnswerId() < 0) {
                    correctAnswer = question.getNewOptions().get((int) -question.getCorrectAnswerId());
                } else {
                    correctAnswer = optionRepository.findById(question.getCorrectAnswerId())
                            .orElseThrow(() -> new ResponseException("Option not found"));
                }
                question.setCorrectAnswer(correctAnswer);
            }
        }

        var unit = unitRepository.findById(unitId).orElseThrow(() -> new ResponseException("Error while retrieving unit"));
        lesson.setUnit(unit);

        lessonRepository.save(lesson);
        return "redirect:/admin/lessons/edit/" + id + "?unitId=" + unitId;
    }

    @GetMapping("/delete/{id}")
    public String deleteLesson(@PathVariable Long id, @RequestParam Long unitId) {
        lessonRepository.deleteById(id);
        return "redirect:/admin/units/edit/" + unitId;
    }

    @DeleteMapping("/options/{id}")
    public ResponseEntity<?> deleteOption(@PathVariable Long id) {
        optionRepository.deleteById(id);

        // Return 204 No Content on success
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        questionRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
