package pawacademy.solution.unit.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pawacademy.solution.lesson.domain.Lesson;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
    private String exam;
    private String image;
    @OneToMany(mappedBy = "unit", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Lesson> lessons;

    @Override
    public Unit clone() {
        return new Unit(this.id, this.name, this.description, this.exam, this.image, lessons);
    }
}
