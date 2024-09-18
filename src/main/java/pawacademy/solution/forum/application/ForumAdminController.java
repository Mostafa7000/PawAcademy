package pawacademy.solution.forum.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pawacademy.ResponseException;
import pawacademy.solution.forum.domain.PostRepository;
import pawacademy.solution.forum.domain.ReplyRepository;

@Controller
@RequestMapping("/admin/posts")
public class ForumAdminController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReplyRepository replyRepository;

    @GetMapping
    public String listPosts(Model model) {
        model.addAttribute("posts", postRepository.findAll());
        return "posts/list";
    }

    @GetMapping("/edit/{id}")
    public String editPostsForm(@PathVariable Long id, Model model) throws ResponseException {
        var post = postRepository.findById(id).orElseThrow(() -> new ResponseException("Post not found"));
        model.addAttribute("post", post);
        return "posts/edit";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePosts(@PathVariable Long id) {
        postRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{post-id}/replies/{reply-id}")
    public ResponseEntity<Object> deleteReply(@PathVariable(value = "post-id", required = false) Long postId, @PathVariable(value = "reply-id") Long replyId) {
        replyRepository.deleteById(replyId);

        return ResponseEntity.noContent().build();
    }
}
