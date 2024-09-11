package pawacademy.solution.user.application;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pawacademy.services.FileStorageService;
import pawacademy.services.UriService;
import pawacademy.solution.user.application.authentication.CurrentUser;
import pawacademy.solution.user.application.dto.AvatarDto;
import pawacademy.solution.user.application.dto.PasswordResetDto;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

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
                fileStorageService.deleteFile(user.getInternalAvatarUrl());
            }
        } catch (NoSuchFileException ignored) {
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Could not delete old avatar");
        }

        String fileName = fileStorageService.storeFile(file, "avatars");
        user.setAvatar(fileName);
        userRepository.save(user);
        var avatarUrl = AvatarDto.builder()
                .avatarUrl(UriService.getUri(fileName))
                .build();
        return ResponseEntity.ok(avatarUrl);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetDto passwordResetDto, @CurrentUser User user) {
        // Verify current password
        if (!passwordEncoder.matches(passwordResetDto.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Current password is incorrect");
        }

        // Update to new password
        user.setPassword(passwordEncoder.encode(passwordResetDto.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }
}
