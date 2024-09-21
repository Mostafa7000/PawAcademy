package pawacademy.solution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pawacademy.services.JwtUtil;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    @Qualifier("adminAuthenticationProvider")
    private AuthenticationProvider adminAuthenticationProvider;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public String admin() {
        return "index";
    }

    // Login form
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        session.invalidate();
        return "login";
    }

    // Login form with error
    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

}
