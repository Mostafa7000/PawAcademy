package pawacademy.solution.forum.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pawacademy.ResponseException;
import pawacademy.solution.forum.domain.BlockedUser;
import pawacademy.solution.forum.domain.BlockedUsersRepository;
import pawacademy.solution.forum.domain.PostRepository;
import pawacademy.solution.forum.domain.ReplyRepository;
import pawacademy.solution.user.domain.User;
import pawacademy.solution.user.domain.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/posts")
public class ForumAdminController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private BlockedUsersRepository blockedUsersRepository;

    @Autowired
    private UserRepository userRepository;


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

    @DeleteMapping("/replies/{reply-id}")
    public ResponseEntity<Object> deleteReply(@PathVariable(value = "reply-id") Long replyId) {
        replyRepository.deleteById(replyId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/blocked")
    public String listBlockedUsers(Model model) {
        List<User> blockedUsers = new ArrayList<>();
        for (var blocked : blockedUsersRepository.findAll()) {
            blockedUsers.add(blocked.getUser());
        }
        model.addAttribute("users", blockedUsers);

        return "posts/blocked";
    }

    @PostMapping("/users/{user-id}/unblock")
    @Transactional
    public ResponseEntity<Object> unblockUser(@PathVariable(value = "user-id") Long userId) throws ResponseException {
        if (!blockedUsersRepository.existsByUserId(userId)) {
            throw new ResponseException("User is not Found");
        }
        blockedUsersRepository.deleteBlockedUserByUserId(userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("users/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam("query") String query) {
        // Assuming userRepository has a method to search by name or email
        return ResponseEntity.ok().body(userRepository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query));
    }

    @PostMapping("/users/{user-id}/block")
    public ResponseEntity<?> blockUser(@PathVariable(value = "user-id") Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (blockedUsersRepository.existsByUserId(userId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User is already blocked");
            }
            BlockedUser blockedUser = new BlockedUser();
            blockedUser.setUser(user);
            blockedUsersRepository.save(blockedUser);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
