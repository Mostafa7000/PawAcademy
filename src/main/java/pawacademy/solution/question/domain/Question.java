package pawacademy.solution.question.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import pawacademy.solution.lesson.domain.Lesson;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "questions")
@Data
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String text;

    @Transient
    private Long correctAnswerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    @ToString.Exclude
    private Lesson lesson;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Transient
    private List<Option> newOptions = new ArrayList<>(Collections.nCopies(10, null));

    @OneToMany(mappedBy = "question", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Option> options = new ArrayList<>();

    @OneToOne
    private Option correctAnswer;

    public Question() {

    }

    public List<Option> getOptions() {
        if (type == QuestionType.TRUE_FALSE) {
            return List.of(Option.getTrue(), Option.getFalse());
        }
        return options;
    }

    public Long getCorrectAnswerId() {
        if (Objects.isNull(correctAnswerId)) {
            correctAnswerId = correctAnswer.getId();
        }

        return correctAnswerId;
    }
}
