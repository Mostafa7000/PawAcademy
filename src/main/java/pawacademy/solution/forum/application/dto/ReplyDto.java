package pawacademy.solution.forum.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDto{
    private Long id;
    private String text;
    private AuthorDto author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isModified;
}
