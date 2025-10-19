package pawacademy.solution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pawacademy.services.FileStorageService;
import pawacademy.solution.services.MediaAuthorizationService;
import pawacademy.solution.user.domain.User;
import pawacademy.solution.user.domain.UserRepository;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/media")
public class MediaController {
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private MediaAuthorizationService mediaAuthorizationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{entity}/{file-name}")
    public ResponseEntity<Resource> uploadAvatar(@PathVariable("file-name") String fileName, @PathVariable String entity) throws MalformedURLException {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = userRepository.findByEmail(principal.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

        if (!mediaAuthorizationService.isAuthorized(user, entity + "/" + fileName)) {
            return ResponseEntity.status(403).build();
        }

        Resource resource = fileStorageService.loadFileAsResource(entity + "/" + fileName);

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
