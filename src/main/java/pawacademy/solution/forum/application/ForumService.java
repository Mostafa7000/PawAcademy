package pawacademy.solution.forum.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pawacademy.ResponseException;
import pawacademy.services.FileStorageService;
import pawacademy.solution.forum.domain.*;
import pawacademy.solution.user.application.authentication.CurrentUser;
import pawacademy.solution.user.domain.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class ForumService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    FileStorageService fileStorageService;

    public Post publish(@CurrentUser User user, String post,String title, MultipartFile[] attachments) {

        Post toBeSavedPost = new Post();
        toBeSavedPost.setText(post);
        toBeSavedPost.setTitle(title);
        toBeSavedPost.setAuthor(user);
        toBeSavedPost.setPostAttachments(getAttachments(attachments, toBeSavedPost));

        return postRepository.save(toBeSavedPost);
    }

    private List<PostAttachment> getAttachments(MultipartFile[] attachments, Post post) {
        var result = new ArrayList<PostAttachment>();
        for (var image : attachments) {
            PostAttachment postAttachment = new PostAttachment();
            postAttachment.setUrl(fileStorageService.storeFile(image, "forum"));
            postAttachment.setPost(post);
            result.add(postAttachment);
        }

        return result;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPost(Long id) throws ResponseException {
        return postRepository.findById(id).orElseThrow(() -> new ResponseException("Post not found"));
    }

    public void deletePost(Long id, User user) throws ResponseException, IOException {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResponseException("Post Not found"));
        validateAuthorization(post, user);
        deleteOldAttachments(post);
        postRepository.deleteById(id);
    }

    public Post editPost(Long id, String post, MultipartFile[] images, User user) throws ResponseException, IOException {
        Post postToEdit = postRepository.findById(id).orElseThrow(() -> new ResponseException("Post Not found"));
        validateAuthorization(postToEdit, user);
        if (!Objects.isNull(post)) {
            postToEdit.setText(post);
        }

        if (!Objects.isNull(images)) {
            deleteOldAttachments(postToEdit);
            postToEdit.setPostAttachments(getAttachments(images, postToEdit));
        }
        postToEdit.setIsModified(true);

        return postRepository.save(postToEdit);
    }

    public Post deletePostAttachment(Long postId, Long attachmentId, User user) throws ResponseException {
        var post = getPost(postId);
        validateAuthorization(post, user);

        Iterator<PostAttachment> it = post.getPostAttachments().iterator();
        while (it.hasNext()) {
            PostAttachment attachment = it.next();
            if (attachment.getId().equals(attachmentId)) {
                it.remove();
                break;
            }
        }

        post.setIsModified(true);

        return postRepository.save(post);
    }

    private void deleteOldAttachments(Post post) throws IOException {
        for (PostAttachment postAttachment : post.getPostAttachments()) {
            fileStorageService.deleteFile(postAttachment.getInternalUrl());
        }
    }

    public Post addPostAttachment(Long postId, MultipartFile image, User user) throws ResponseException, IOException {
        var post = getPost(postId);
        validateAuthorization(post, user);

        post.getPostAttachments().add(getAttachments(new MultipartFile[]{image}, post).get(0));
        post.setIsModified(true);

        return postRepository.save(post);
    }

    public Post addPostReply(User author, Long postId, String reply) throws ResponseException {
        var post = getPost(postId);
        Reply newReply = new Reply();
        newReply.setText(reply);
        newReply.setPost(post);
        newReply.setAuthor(author);

        post.getReplies().add(newReply);
        return postRepository.save(post);
    }

    public Post deletePostReply(Long postId, Long replyId, @CurrentUser User user) throws ResponseException {
        var post = getPost(postId);

        Iterator<Reply> it = post.getReplies().iterator();
        while (it.hasNext()) {
            Reply reply = it.next();
            if (reply.getId().equals(replyId)) {
                validateAuthorization(reply, user);
                it.remove();
                break;
            }
        }

        return postRepository.save(post);
    }

    public Post editPostReply(Long postId, Long replyId, String newText, @CurrentUser User user) throws ResponseException, IOException {
        var post = getPost(postId);
        validateAuthorization(post, user);

        Iterator<Reply> it = post.getReplies().iterator();
        while (it.hasNext()) {
            Reply reply = it.next();
            if (reply.getId().equals(replyId)) {
                validateAuthorization(reply, user);
                reply.setText(newText);
                reply.setIsModified(true);
                break;
            }
        }

        return postRepository.save(post);
    }

    protected void validateAuthorization(Content content, User user) {
        if (!Objects.equals(user.getId(), content.getAuthor().getId())) {
            throw new AuthenticationCredentialsNotFoundException("Current user can't operate on this content");
        }
    }
}
