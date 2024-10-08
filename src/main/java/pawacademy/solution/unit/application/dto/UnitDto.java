package pawacademy.solution.unit.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pawacademy.solution.question.application.dto.QuestionDto;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitDto{
    private Long id;
    private String name;
    private String description;
    private String image;
    private List<QuestionDto> examQuestions;
    private int completedLessons;
    private int totalLessons;

    public String getImage() {
        return !Objects.isNull(image) ? ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/" + image)
                .toUriString() : null;
    }
}
