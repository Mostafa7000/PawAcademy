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
import pawacademy.solution.user.application.dto.ResetPasswordDto;
import pawacademy.solution.user.application.dto.TokenDto;
import pawacademy.solution.user.application.dto.UserLoginDto;
import pawacademy.solution.user.application.dto.UserRegistrationDto;

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
    public ResponseEntity<?> authenticateUser(@RequestBody UserLoginDto loginRequest) throws ResponseException {
        var authentication = authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        if (!userService.isEmailVerified(loginRequest.getEmail())) {
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

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token, Model model) {
        try {
            userService.verifyUser(token);
        } catch (ResponseException e) {
            return "verification-error";
        }

        return "verification-success";
    }

    @PostMapping("/forgetPassword")
    @ResponseBody
    public String forgetPassword(@RequestParam String email) throws ResponseException {
        userService.SendResetPasswordTokenTo(email);

        return "Reset password token sent successfully";
    }

    @PostMapping("/resetPassword")
    @ResponseBody
    public String resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) throws ResponseException {
        userService.resetPassword(resetPasswordDto);

        return "Password reset successfully";
    }

}
