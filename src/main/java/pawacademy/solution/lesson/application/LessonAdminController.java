package pawacademy.solution.lesson.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import pawacademy.solution.unit.domain.UnitRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/lessons")
public class LessonAdminController {

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private OptionRepository optionRepository;
    private ModelMapper mapper = new ModelMapper();

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
        model.addAttribute("lesson", lessonRepository.findById(id).orElse(new Lesson()));
        model.addAttribute("unitId", unitId);
        return "lessons/edit";
    }

    @PostMapping("/update/{id}")
    @Transactional
    public String updateLesson(@PathVariable Long id, @ModelAttribute("lesson") Lesson lesson, @RequestParam Long unitId, String newOptions) throws ResponseException {
        // Parse the newOptions JSON
        ObjectMapper objectMapper = new ObjectMapper();
        Map<Long, List<Map<String, Object>>> newOptionsMap;
        try {
            newOptionsMap = objectMapper.readValue(newOptions, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new ResponseException("Error parsing new options JSON");
        }

        for (Question question : lesson.getQuestions()) {
            question.setLesson(lesson);
            if (question.getCorrectAnswerId() != null) {
                question.setCorrectAnswer(optionRepository.findById(question.getCorrectAnswerId())
                        .orElseThrow(() -> new ResponseException("Option not found"))
                );
            }
            for (Option option : question.getOptions()) {
                option.setQuestion(question);
            }

            // Check if there are new options for this question in the newOptions JSON
            if (newOptionsMap.containsKey(question.getId())) {
                List<Map<String, Object>> optionsList = newOptionsMap.get(question.getId());

                // Add new options from the newOptions JSON
                for (Map<String, Object> optionData : optionsList) {
                    Option newOption = new Option();
                    newOption.setText((String) optionData.get("text"));
                    if (optionData.get("correct").equals(1)) {
                        question.setCorrectAnswer(newOption);
                    }
                    newOption.setQuestion(question);  // Link option to the question
                    question.getOptions().add(newOption);  // Add the new option to the question's options
                }
            }
        }

        var unit = unitRepository.findById(unitId).orElseThrow(() -> new ResponseException("Error while retrieving unit"));
        lesson.setUnit(unit);

        lessonRepository.save(lesson);
        // lessonRepository.update(id, lesson.getName(), lesson.getContent(), lesson.getVideo());
        return "redirect:/admin/units/edit/" + unitId;
    }

    @GetMapping("/delete/{id}")
    public String deleteLesson(@PathVariable Long id, @RequestParam Long unitId) {
        lessonRepository.deleteById(id);
        return "redirect:/admin/units/edit/" + unitId;
    }

    @DeleteMapping("/options/{id}")
    public void deleteOption(@PathVariable Long id) {
        optionRepository.deleteById(id);
    }
}
