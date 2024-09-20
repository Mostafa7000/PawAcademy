package pawacademy.solution.unit.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamTrialDto {
    private UnitDto unit;
    private Double score;
    private LocalDateTime completedAt = LocalDateTime.now(); // Timestamp of completion
}
