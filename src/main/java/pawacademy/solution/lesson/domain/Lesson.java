package pawacademy.solution.lesson.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import pawacademy.solution.question.domain.Question;
import pawacademy.solution.unit.domain.Unit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    private String video;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Question> questions = new ArrayList<>();

    @Transient
    private List<Question> newQuestions = new ArrayList<>(Collections.nCopies(10, null));

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    @ToString.Exclude
    private Unit unit;
}
