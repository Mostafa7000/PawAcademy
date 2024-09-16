package pawacademy.solution.question.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "options")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @ToString.Exclude
    private Question question;

    @NotBlank
    private String text;
    public static Option getTrue() {
        Option option = new Option();
        option.setId(1L);
        option.setText("True");
        return option;
    }

    public static Option getFalse() {
        Option option = new Option();
        option.setId(2L);
        option.setText("False");
        return option;
    }
}
