package pawacademy.solution.user.application;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pawacademy.ResponseException;
import pawacademy.services.EmailService;
import pawacademy.solution.user.application.dto.UserRegistrationDto;
import pawacademy.solution.user.domain.User;
import pawacademy.solution.user.domain.UserRepository;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public void registerUser(UserRegistrationDto newUserDto) throws ResponseException {
        var savedUser = new User();
        if (userRepository.existsByEmail(newUserDto.getEmail())) {
            throw new ResponseException("User already exists");
        }
        ModelMapper mapper = new ModelMapper();
        mapper.map(newUserDto, savedUser);

        savedUser.setPassword(passwordEncoder.encode(newUserDto.getPassword()));
        savedUser.setVerificationToken(UUID.randomUUID().toString());
        userRepository.save(savedUser);
        sendVerificationEmail(savedUser, savedUser.getVerificationToken());
    }

    public void verifyUser(String token) throws ResponseException {
        User user = userRepository.findByVerificationToken(token).orElseThrow(() -> new ResponseException("User not found"));
        user.setVerified(true);
        userRepository.save(user);
    }

    private void sendVerificationEmail(User user, String token) {
        emailService.sendVerificationEmail(user, token);
    }
}
