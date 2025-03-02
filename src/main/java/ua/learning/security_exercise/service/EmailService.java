package ua.learning.security_exercise.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @SneakyThrows
    public void sendActivationEmail(String email, String key) {
        String html = """
                <html>
                <body>
                    <h3>Account activation</h3>
                    <a href="http://localhost:8089/activation/code">Activate account</a>
                </body>
                </html>
                """;
        html = html.replaceAll("code", key);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        messageHelper.setTo(email);
        messageHelper.setSubject("Account activation");
        messageHelper.setText(html, true);
        mailSender.send(message);
    }

    @SneakyThrows
    public void sendWelcomeEmail(String email, String password) {
        String html = """
                <html>
                <body>
                    <h3>Thank you for registration</h3>
                    <p>Your password: code</p>
                    <a href="http://localhost:8089/auth/login">Log in</a>
                </body>
                </html>
                """;
        html = html.replaceAll("code", password);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        messageHelper.setTo(email);
        messageHelper.setSubject("Welcome message");
        messageHelper.setText(html, true);
        mailSender.send(message);
    }
}
