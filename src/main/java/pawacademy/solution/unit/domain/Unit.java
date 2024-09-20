package pawacademy.solution.unit.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import pawacademy.services.UriService;
import pawacademy.solution.lesson.domain.Lesson;
import pawacademy.solution.question.domain.Question;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "units")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Unit implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private String image;
    @OneToMany(mappedBy = "unit", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Lesson> lessons = new ArrayList<>();

    @OneToMany(mappedBy = "unit", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    @Fetch(FetchMode.SUBSELECT)
    private List<Question> examQuestions = new ArrayList<>();

    @Transient
    private List<Question> newExamQuestions = new ArrayList<>();

    public String getImage() {
        return UriService.getUri(image);
    }

    public String getInternalImageUrl() {
        return image;
    }
}
