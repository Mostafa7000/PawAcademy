package pawacademy.solution.forum.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pawacademy.AuthorizationException;
import pawacademy.solution.forum.domain.Post;
import pawacademy.solution.forum.domain.PostRepository;
import pawacademy.solution.forum.domain.Reply;
import pawacademy.solution.user.domain.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ForumServiceTest {
    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private ForumService forumService;
    private User user;
    private User anotherUser;
    private Post post;
    private Reply reply;
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        anotherUser = new User();
        anotherUser.setId(2L);
        post = new Post();
        post.setId(1L);
        post.setAuthor(user);
        reply = new Reply();
        reply.setId(1L);
        reply.setAuthor(anotherUser);
        List<Reply> replies = new ArrayList<>();
        replies.add(reply);
        post.setReplies(replies);
    }

    @Test
    void testEditPostReply() throws IOException {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);
        assertDoesNotThrow(() -> forumService.editPostReply(1L, 1L, "new text", anotherUser));
        assertEquals("new text", reply.getText());
        assertTrue(reply.getIsModified());
    }

    @Test
    void testEditPostReplyWithWrongUser() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        assertThrows(AuthorizationException.class, () -> forumService.editPostReply(1L, 1L, "new text", user));
    }
}
