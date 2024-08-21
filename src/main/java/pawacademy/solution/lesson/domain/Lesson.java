package pawacademy.solution.lesson.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pawacademy.solution.quiz.domain.Quiz;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "lessons")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String content;
    private String video;
    @OneToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Override
    public Lesson clone() {
        return new Lesson(this.id, this.name, this.content, this.video, this.quiz);
    }
}
