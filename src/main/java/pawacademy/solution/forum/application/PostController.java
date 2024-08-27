package pawacademy.solution.forum.application;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pawacademy.ResponseException;
import pawacademy.services.UriService;
import pawacademy.solution.forum.application.dto.PostDto;
import pawacademy.solution.user.application.authentication.CurrentUser;
import pawacademy.solution.user.domain.User;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private ForumService forumService;
    ModelMapper mapper = new ModelMapper();

    @PostMapping
    public ResponseEntity<?> publishPost(
            @CurrentUser User user,
            @RequestPart("post") String post,
            @RequestPart("images") MultipartFile[] attachments
    ) {
        var publishedPost = forumService.publish(user, post, attachments);

        return ResponseEntity.created(getRelocationUri("posts/" + publishedPost.getId())).build();
    }

    private URI getRelocationUri(String internalPath) {
        return URI.create(Objects.requireNonNull(UriService.getUri(internalPath)));
    }

    @GetMapping
    public List<PostDto> getAll() {
        var posts = forumService.getAllPosts();
        List<PostDto> result = new ArrayList<>();

        for (var post : posts) {
            result.add(mapper.map(post, PostDto.class));
        }

        return result;
    }

    @GetMapping("/{postId}")
    public PostDto getPost(@PathVariable Long postId) throws ResponseException {
        var post = forumService.getPost(postId);

        return mapper.map(post, PostDto.class);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) throws ResponseException, IOException {
        forumService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<?> editPost(
            @PathVariable Long postId,
            @RequestPart(value = "post", required = false) String post,
            @RequestPart(value = "images", required = false) MultipartFile[] attachments
    ) throws ResponseException, IOException {
        forumService.editPost(postId, post, attachments);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}/attachments/{attachmentId}")
    public PostDto deleteAttachment(@PathVariable Long postId, @PathVariable Long attachmentId) throws ResponseException {
        var modifiedPost = forumService.deletePostAttachment(postId, attachmentId);

        return mapper.map(modifiedPost, PostDto.class);
    }

    @PostMapping("/{postId}/attachments")
    public ResponseEntity<?> addAttachment(@PathVariable Long postId, @RequestParam MultipartFile attachment) throws ResponseException, IOException {
        forumService.addPostAttachment(postId, attachment);

        return ResponseEntity.created(getRelocationUri("posts/" + postId)).build();
    }
}
