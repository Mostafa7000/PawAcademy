package pawacademy.solution.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserEditingDto {
    @Email
    private String email;

    private String firstName;

    private String lastName;

    @Past
    private LocalDate birthDate;

    private String gender;

    @Override
    public UserEditingDto clone() {
        return new UserEditingDto(this.email, this.firstName, this.lastName, this.birthDate, this.gender);
    }
}
