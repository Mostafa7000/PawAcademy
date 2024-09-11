package pawacademy.solution.lesson.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pawacademy.solution.unit.domain.UnitRepository;

@Controller
@RequestMapping("/admin/lessons")
public class LessonAdminController {

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UnitRepository unitRepository;

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
    public String updateLesson(@PathVariable Long id, @ModelAttribute("lesson") Lesson lesson, @RequestParam Long unitId) {
        lessonRepository.update(id, lesson.getName(), lesson.getContent(), lesson.getVideo());
        return "redirect:/admin/units/edit/" + unitId;
    }

    @GetMapping("/delete/{id}")
    public String deleteLesson(@PathVariable Long id, @RequestParam Long unitId) {
        lessonRepository.deleteById(id);
        return "redirect:/admin/units/edit/" + unitId;
    }
}
