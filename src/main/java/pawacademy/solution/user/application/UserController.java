package pawacademy.solution.user.application;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pawacademy.services.FileStorageService;
import pawacademy.solution.user.application.authentication.CurrentUser;
import pawacademy.solution.user.application.dto.AvatarDto;
import pawacademy.solution.user.application.dto.UserEditingDto;
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
    public ResponseEntity<?> profile(@CurrentUser User user) {
        return ResponseEntity.ok(user);
    }

    @PatchMapping
    public ResponseEntity<?> editProfile(@Valid @RequestBody UserEditingDto updatedUser, @CurrentUser User existingUser) {
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
    public ResponseEntity<?> uploadAvatar(@RequestPart("image") MultipartFile file, @CurrentUser User user) {
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
        var avatarDto = AvatarDto.builder().avatarUrl(fileName).build();
        return ResponseEntity.ok(avatarDto);
    }

}
