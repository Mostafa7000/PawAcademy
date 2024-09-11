package pawacademy.solution.unit.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pawacademy.services.FileStorageService;
import pawacademy.solution.lesson.domain.LessonRepository;
import pawacademy.solution.unit.domain.Unit;
import pawacademy.solution.unit.domain.UnitRepository;

import java.io.IOException;

@Controller
@RequestMapping("/admin/units")
public class UnitAdminController {

    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public String listUnits(Model model) {
        model.addAttribute("units", unitRepository.findAll());
        return "units/list";
    }

    @GetMapping("/new")
    public String createUnitForm(Model model) {
        model.addAttribute("unit", new Unit());
        return "units/create";
    }

    @PostMapping
    public String saveUnit(@ModelAttribute("unit") Unit unit, @RequestPart("uploadedImage") MultipartFile image) {
        saveUnitImage(unit, image);
        return "redirect:/admin/units";
    }

    @GetMapping("/edit/{id}")
    public String editUnitForm(@PathVariable Long id, Model model) {
        var unit = unitRepository.findById(id).orElse(new Unit());
        var lessons = lessonRepository.findAllByUnitId(id);
        model.addAttribute("unit", unit);
        model.addAttribute("lessons", lessons);
        return "units/edit";
    }

    @PostMapping("/update/{id}")
    public String updateUnit(@PathVariable Long id, @ModelAttribute("unit") Unit unit, @RequestPart("uploadedImage") MultipartFile image) {
        unit.setId(id);
        saveUnitImage(unit, image);
        return "redirect:/admin/units";
    }

    private void saveUnitImage(Unit unit, MultipartFile image) {
        try {
            String oldImage = unitRepository.findImageByUnitId(unit.getId());
            if (oldImage != null) {
                fileStorageService.deleteFile(oldImage);
            }
        } catch (IOException ignored) {
        }

        String fileName = fileStorageService.storeFile(image, "units");
        unit.setImage(fileName);
        unitRepository.save(unit);
    }

    @GetMapping("/delete/{id}")
    public String deleteUnit(@PathVariable Long id) {
        unitRepository.deleteById(id);
        return "redirect:/admin/units";
    }
}
