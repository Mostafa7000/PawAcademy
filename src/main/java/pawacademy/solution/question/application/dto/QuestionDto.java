package pawacademy.solution.question.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pawacademy.solution.question.domain.QuestionType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private Long id;
    private QuestionType type;
    private String text;
    private List<OptionDto> options;
    private OptionDto correctAnswer;
}
