package pawacademy.solution.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pawacademy.solution.user.application.PasswordComplexity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @NotNull
    @Column(unique = true)
    private String email;

    @NotBlank
    @NotNull
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @PasswordComplexity
    private String password;

    @NotBlank
    @NotNull
    private String firstName;

    @NotBlank
    @NotNull
    private String lastName;

    private String avatar;  // Store the file name here

    @Past
    @NotNull
    private LocalDate birthDate;

    @NotNull
    @NotBlank
    private String gender;

    @Override
    public User clone() {
        return new User(this.id, this.email, this.password, this.firstName, this.lastName, this.avatar, this.birthDate, this.gender);
    }

    public String getAvatar() {
        return !Objects.isNull(avatar) ? ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/" + avatar)
                .toUriString() : null;
    }
}
