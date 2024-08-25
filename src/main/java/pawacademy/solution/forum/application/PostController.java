package pawacademy.solution.forum.application;

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

    @PostMapping
    public ResponseEntity<?> publishPost(
            @CurrentUser User user,
            @RequestPart("post") String post,
            @RequestPart("images") MultipartFile[] attachments
    ) {
        var publishedPost = forumService.publish(user, post, attachments);

        return ResponseEntity.created(URI.create(Objects.requireNonNull(UriService.getUri("posts/" + publishedPost.getId())))).build();
    }

    @GetMapping
    public List<PostDto> getAll() {
        var posts = forumService.getAllPosts();
        List<PostDto> result = new ArrayList<>();

        for (var post : posts) {
            var attachmentsUrls = new ArrayList<String>();
            for (var attachment : post.getPostAttachments()) {
                attachmentsUrls.add(attachment.getUrl());
            }
            result.add(new PostDto(
                    post.getId(),
                    post.getPost(),
                    attachmentsUrls,
                    post.getAuthor().getFullName(),
                    post.getAuthor().getEmail())
            );
        }

        return result;
    }

    @GetMapping("/{postId}")
    public PostDto getPost(@PathVariable Long postId) throws ResponseException {
        var post = forumService.getPost(postId);
        var attachmentsUrls = new ArrayList<String>();
        for (var attachment : post.getPostAttachments()) {
            attachmentsUrls.add(attachment.getUrl());
        }

        return new PostDto(post.getId(), post.getPost(), attachmentsUrls, post.getAuthor().getFullName(), post.getAuthor().getEmail());
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
}
