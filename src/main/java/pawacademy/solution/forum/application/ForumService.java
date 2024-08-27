package pawacademy.solution.forum.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pawacademy.ResponseException;
import pawacademy.services.FileStorageService;
import pawacademy.solution.forum.domain.Post;
import pawacademy.solution.forum.domain.PostAttachment;
import pawacademy.solution.forum.domain.PostRepository;
import pawacademy.solution.forum.domain.ReplyRepository;
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

    public Post publish(@CurrentUser User user, String post, MultipartFile[] attachments) {

        Post toBeSavedPost = new Post();
        toBeSavedPost.setPost(post);
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

    public void deletePost(Long id) throws ResponseException, IOException {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResponseException("Post Not found"));
        deleteOldAttachments(post);
        postRepository.deleteById(id);
    }

    public void editPost(Long id, String post, MultipartFile[] images) throws ResponseException, IOException {
        Post postToEdit = postRepository.findById(id).orElseThrow(() -> new ResponseException("Post Not found"));
        if (!Objects.isNull(post)) {
            postToEdit.setPost(post);
        }

        if (!Objects.isNull(images)) {
            deleteOldAttachments(postToEdit);
            postToEdit.setPostAttachments(getAttachments(images, postToEdit));
        }

        postRepository.save(postToEdit);
    }

    private void deleteOldAttachments(Post post) throws IOException {
        for (PostAttachment postAttachment : post.getPostAttachments()) {
            fileStorageService.deleteFile(postAttachment.getInternalUrl());
        }
    }

    public Post deletePostAttachment(Long postId, Long attachmentId) throws ResponseException {
        var post = getPost(postId);

        Iterator<PostAttachment> it = post.getPostAttachments().iterator();
        while (it.hasNext()) {
            PostAttachment attachment = it.next();
            if (attachment.getId().equals(attachmentId)) {
                it.remove();
                break;
            }
        }
        return postRepository.save(post);
    }

    public Post addPostAttachment(Long postId, MultipartFile image) throws ResponseException, IOException {
        var post = getPost(postId);
        post.getPostAttachments().add(getAttachments(new MultipartFile[]{image}, post).get(0));
        return postRepository.save(post);
    }
}
