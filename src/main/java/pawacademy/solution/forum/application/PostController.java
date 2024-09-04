package pawacademy.solution.forum.application;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pawacademy.ResponseException;
import pawacademy.services.UriService;
import pawacademy.solution.forum.application.dto.PostDto;
import pawacademy.solution.forum.application.dto.ReplyDto;
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
            @RequestPart("title") String title,
            @RequestPart("images") MultipartFile[] attachments
    ) {
        var publishedPost = forumService.publish(user, post, title, attachments);

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
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @CurrentUser User user) throws ResponseException, IOException {
        forumService.deletePost(postId, user);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{postId}")
    public PostDto editPost(
            @PathVariable Long postId,
            @RequestPart(value = "post", required = false) String post,
            @RequestPart(value = "images", required = false) MultipartFile[] attachments,
            @CurrentUser User user
    ) throws ResponseException, IOException {
        var modifiedPost = forumService.editPost(postId, post, attachments, user);

        return mapper.map(modifiedPost, PostDto.class);
    }

    @DeleteMapping("/{postId}/attachments/{attachmentId}")
    public PostDto deleteAttachment(@PathVariable Long postId, @PathVariable Long attachmentId, @CurrentUser User user) throws ResponseException {
        var modifiedPost = forumService.deletePostAttachment(postId, attachmentId, user);

        return mapper.map(modifiedPost, PostDto.class);
    }

    @PostMapping("/{postId}/attachments")
    public PostDto addAttachment(@PathVariable Long postId, @RequestParam MultipartFile attachment, @CurrentUser User user) throws ResponseException, IOException {
        var modifiedPost = forumService.addPostAttachment(postId, attachment, user);

        return mapper.map(modifiedPost, PostDto.class);
    }

    @PostMapping("/{postId}/replies")
    public PostDto addReply(@PathVariable Long postId, @RequestPart("reply") String reply, @CurrentUser User user) throws ResponseException {
        var modifiedPost = forumService.addPostReply(user, postId, reply);

        return mapper.map(modifiedPost, PostDto.class);
    }

    @GetMapping("/{postId}/replies")
    public List<ReplyDto> getReplies(@PathVariable Long postId, @CurrentUser User user) throws ResponseException {
        var post = forumService.getPost(postId);

        var result = new ArrayList<ReplyDto>();
        for (var reply : post.getReplies()) {
            result.add(mapper.map(reply, ReplyDto.class));
        }
        return result;
    }

    @DeleteMapping("/{postId}/replies/{replyId}")
    public PostDto deleteReply(@PathVariable Long postId, @PathVariable Long replyId, @CurrentUser User user) throws ResponseException {
        var modifiedPost = forumService.deletePostReply(postId, replyId, user);

        return mapper.map(modifiedPost, PostDto.class);
    }

    @PatchMapping("/{postId}/replies/{replyId}")
    public PostDto editReply(@PathVariable Long postId, @PathVariable Long replyId, @RequestParam String newReply, @CurrentUser User user) throws ResponseException, IOException {
        var modifiedPost = forumService.editPostReply(postId, replyId, newReply, user);

        return mapper.map(modifiedPost, PostDto.class);
    }
}
