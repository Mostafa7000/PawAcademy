package pawacademy.solution.user.application;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pawacademy.ResponseException;
import pawacademy.solution.user.application.dto.UserRegistrationDto;
import pawacademy.solution.user.domain.User;
import pawacademy.solution.user.domain.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(UserRegistrationDto newUserDto) throws ResponseException {
        var savedUser = new User();
        if (userRepository.existsByEmail(newUserDto.getEmail())) {
            throw new ResponseException("User already exists");
        }
        ModelMapper mapper = new ModelMapper();
        mapper.map(newUserDto, savedUser);

        savedUser.setPassword(passwordEncoder.encode(newUserDto.getPassword()));
        userRepository.save(savedUser);
    }
}
