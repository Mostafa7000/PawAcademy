package pawacademy.solution.user.application;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pawacademy.solution.user.domain.User;
import pawacademy.solution.user.domain.UserRepository;

import javax.validation.Valid;

@RestController
@RequestMapping("/profile")
public class UserController {
    @Autowired
    private UserRepository userRepository;

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
}
