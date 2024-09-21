package pawacademy.solution.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import pawacademy.solution.user.domain.Admin;
import pawacademy.solution.user.domain.AdminRepository;

@ShellComponent
public class AdminRegistration {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminRegistration(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
    }

    @ShellMethod("Register admin")
    public String register(@ShellOption(value = "--username", defaultValue = "admin") String username,
                           @ShellOption(value = "--password", defaultValue = "admin1234" ) String password,
                           @ShellOption(value = "--fullname", defaultValue = "Administrator") String fullname) {
        if (adminRepository.existsByUsername(username)) {
            return "Admin already exists";
        }
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setFullName(fullname);
        adminRepository.save(admin);

        return "Registration successful for Admin " + username;
    }
}
