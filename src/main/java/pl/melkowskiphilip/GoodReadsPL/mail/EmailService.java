package pl.melkowskiphilip.GoodReadsPL.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.melkowskiphilip.GoodReadsPL.entity.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendActivationEmail(User user, String token) {
        String activationLink = "http://localhost:8080/api/auth/activation?token=" + token;

        String subject = "Aktywacja konta w GoodReadsPL";
        String htmlMessage = buildActivationEmail(user.getUsername(), activationLink);

        sendHtmlEmail(user.getEmail(), subject, htmlMessage);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new IllegalStateException("Nie udało się wysłać emaila: " + e.getMessage());
        }
    }

    private String buildActivationEmail(String username, String activationLink) {
        return """
                <html>
                <body>
                    <h2>Witaj, %s!</h2>
                    <p>Dziękujemy za rejestrację w <strong>GoodReadsPL</strong>.</p>
                    <p>Aby aktywować swoje konto, kliknij w poniższy link:</p>
                    <p>
                        <a href="%s" style="padding:10px 20px; background:#4CAF50; color:white; text-decoration:none; border-radius:5px;">
                            Aktywuj konto
                        </a>
                    </p>
                    <p>Jeśli to nie Ty zakładałeś konto, zignoruj tę wiadomość.</p>
                    <hr/>
                    <p style="font-size:12px; color:#888;">Link wygasa po 7 dniach.</p>
                </body>
                </html>
                """.formatted(username, activationLink);
    }
}