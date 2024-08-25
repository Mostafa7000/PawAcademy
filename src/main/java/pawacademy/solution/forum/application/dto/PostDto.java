package pawacademy.solution.forum.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String post;
    private List<String> attachments;
    private String authorName;
    private String authorEmail;
}
