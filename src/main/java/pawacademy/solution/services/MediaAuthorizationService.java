package pawacademy.solution.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pawacademy.solution.forum.domain.PostRepository;
import pawacademy.solution.user.domain.User;
import pawacademy.solution.user.domain.UserRepository;

@Service
public class MediaAuthorizationService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean isAuthorized(User user, String filePath) {
        String[] pathParts = filePath.split("/");
        if (pathParts.length < 2) {
            return false;
        }

        String entityType = pathParts[0];
        String fileName = pathParts[1];

        switch (entityType) {
            case "forum":
                // For forum attachments, we need to find the post that the attachment belongs to.
                // Since we don't have a direct link from the file to the post, we'll need to query the database.
                // This is not ideal, but it's the only way to implement the authorization check without changing the database schema.
                return postRepository.findByPostAttachments_UrlContaining(fileName)
                        .map(post -> post.getAuthor().getId().equals(user.getId()))
                        .orElse(false);
            case "avatars":
                // For avatars, the file name should be the user's ID.
                // We can check if the user is requesting their own avatar.
                return userRepository.findByAvatar(fileName)
                        .map(owner -> owner.getId().equals(user.getId()))
                        .orElse(false);
            case "units":
                // Unit images are public to all authenticated users.
                return true;
            default:
                return false;
        }
    }
}
