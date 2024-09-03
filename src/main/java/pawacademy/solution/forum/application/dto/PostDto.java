package pawacademy.solution.forum.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto extends ReplyDto {
    private String title;
    private List<AttachmentDto> attachments;
    private List<ReplyDto> replies;
}
