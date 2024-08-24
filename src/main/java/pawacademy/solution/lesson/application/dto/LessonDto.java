package pawacademy.solution.lesson.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pawacademy.solution.question.application.dto.QuestionDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDto {
    private Long id;
    private String name;
    private String content;
    private String video;
    private List<QuestionDto> questions;
}
