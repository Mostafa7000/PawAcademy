package pawacademy.solution.unit.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ExamAnswerDto {
    List<QuestionOptionDto> answers;
}
