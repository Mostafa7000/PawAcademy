package pawacademy.solution.user.application;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pawacademy.solution.user.FileStorageService;
import pawacademy.solution.user.domain.User;
import pawacademy.solution.user.domain.UserRepository;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

@RestController
@RequestMapping("/profile")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<?> profile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        return ResponseEntity.ok(user);
    }

    @PatchMapping
    public ResponseEntity<?> editProfile(@Valid @RequestBody UserEditingDto updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User existingUser = userRepository.findByEmail(email).orElse(null);

        if (existingUser != null) {
            var mapper = new ModelMapper();
            mapper.getConfiguration().setSkipNullEnabled(true);
            mapper.map(updatedUser, existingUser);
            userRepository.save(existingUser);
            return ResponseEntity.ok(existingUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("image") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        try {
            if (user.getAvatar() != null) {
                fileStorageService.deleteFile(user.getAvatar().substring(user.getAvatar().indexOf("media/")));
            }
        } catch (NoSuchFileException ignored) {

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Could not delete old avatar");
        }

        String fileName = fileStorageService.storeFile(file, "avatars");
        user.setAvatar(fileName);
        userRepository.save(user);

        return ResponseEntity.ok(fileName);
    }

}
