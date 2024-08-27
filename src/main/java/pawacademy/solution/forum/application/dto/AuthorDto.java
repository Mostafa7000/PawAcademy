package pawacademy.solution.forum.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {
    private Long id;
    private String email;
    private String fullName;
    private String avatar;  // Store the file name here
    private LocalDate birthDate;
    private String gender;
}
