package pawacademy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pawacademy.solution.user.domain.User;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(User user, String token) {
        String recipientAddress = user.getEmail();
        String subject = "Email Verification";

        StringBuilder confirmationUrl = new StringBuilder(UriService.getUri("auth/registrationConfirm")).append("?token=").append(token);

        String message = "Please verify your email by clicking the link below:\n";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + confirmationUrl);
        mailSender.send(email);
    }
}
