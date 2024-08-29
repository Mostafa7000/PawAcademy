package pawacademy.solution.user.application;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pawacademy.ResponseException;
import pawacademy.services.EmailService;
import pawacademy.solution.user.application.dto.ResetPasswordDto;
import pawacademy.solution.user.application.dto.UserRegistrationDto;
import pawacademy.solution.user.domain.User;
import pawacademy.solution.user.domain.UserRepository;

import java.security.SecureRandom;
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

    public void SendResetPasswordTokenTo(String email) throws ResponseException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseException("User not found"));
        user.setRpToken(generateOtp());
        userRepository.save(user);
        emailService.sendResetPasswordToken(email, user.getRpToken());
    }

    private String generateOtp() {
        SecureRandom secureRandom = new SecureRandom();
        int otp = secureRandom.nextInt(1000, 10000);

        return String.valueOf(otp);
    }

    public void resetPassword(ResetPasswordDto resetPasswordDto) throws ResponseException {
        User user = userRepository.findByEmail(resetPasswordDto.getEmail()).orElseThrow(() -> new ResponseException("User not found"));

        if (!user.getRpToken().equals(resetPasswordDto.getOtp())) {
            throw new ResponseException("Invalid OTP");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        user.setRpToken(null);
        userRepository.save(user);
    }

    public boolean isEmailVerified(String email) throws ResponseException {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseException("User not found")).isVerified();
    }
}
