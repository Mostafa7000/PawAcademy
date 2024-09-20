package pawacademy.solution.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pawacademy.services.UriService;
import pawacademy.solution.unit.application.dto.ExamTrialDto;
import pawacademy.solution.user.application.validation.PasswordComplexity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

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
    private String fullName;

    private String avatar;  // Store the file name here

    @Past
    @NotNull
    private LocalDate birthDate;

    @NotNull
    @NotBlank
    private String gender;

    private String verificationToken;
    private boolean isVerified = false;
    private String rpToken;

    @Transient
    private List<ExamTrialDto> examTrials;

    public String getAvatar() {
        return UriService.getUri(avatar);
    }
    public String getInternalAvatarUrl() {
        return avatar;
    }
}
