package pawacademy.solution.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import pawacademy.solution.user.application.validation.PasswordComplexity;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UserRegistrationDto {
    @Email
    @NotBlank
    @NotNull
    private String email;

    @NotBlank
    @NotNull
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @PasswordComplexity
    private String password;

    @NotBlank
    @NotNull
    private String fullName;

    @Past
    @NotNull
    private LocalDate birthDate;

    @NotNull
    @NotBlank
    private String gender;

    @Override
    public UserRegistrationDto clone() {
        return new UserRegistrationDto(this.email, this.password, this.fullName, this.birthDate, this.gender);
    }
}
