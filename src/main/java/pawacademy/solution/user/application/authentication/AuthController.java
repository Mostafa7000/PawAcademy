package pawacademy.solution.user.application.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pawacademy.ResponseException;
import pawacademy.services.JwtUtil;
import pawacademy.solution.user.application.UserService;
import pawacademy.solution.user.application.dto.TokenDto;
import pawacademy.solution.user.application.dto.UserLoginDto;
import pawacademy.solution.user.application.dto.UserRegistrationDto;
import pawacademy.solution.user.domain.User;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    @ResponseBody
    public String registerUser(@Valid @RequestBody UserRegistrationDto newUser) throws ResponseException {
        userService.registerUser(newUser);

        return "Verification email sent successfully";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> authenticateUser(@RequestBody UserLoginDto loginRequest) {
        var authentication = authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        if (!isEmailVerified(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email not verified");
        }

        String jwt = jwtUtil.generateToken(loginRequest.getEmail());
        return ResponseEntity.ok(new TokenDto(jwt));
    }

    private Authentication authenticate(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

    private boolean isEmailVerified(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.isVerified();
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token, Model model) {
        try {
            userService.verifyUser(token);
        } catch (ResponseException e) {
            return "verification-error";
        }

        return "verification-success";
    }
}
