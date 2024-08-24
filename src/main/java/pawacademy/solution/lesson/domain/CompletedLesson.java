package pawacademy.solution.lesson.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pawacademy.solution.user.domain.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "completed_lessons")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletedLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
    private LocalDateTime completedAt = LocalDateTime.now(); // Timestamp of completion
}
