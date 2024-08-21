package pawacademy.solution.unit.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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

    @Override
    public Unit clone() {
        return new Unit(this.id, this.name, this.description, this.exam, this.image);
    }
}
