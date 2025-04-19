package com.catalyst.TicketingSystemApi.service.impl;

import com.catalyst.TicketingSystemApi.model.User;
import com.catalyst.TicketingSystemApi.repository.UserRepository;
import com.catalyst.TicketingSystemApi.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    public void sendResetPasswordEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException {
        try {
            User user = userRepository.findByEmail(email);
            if (user != null) {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);

                helper.setFrom("contact@catalyst.com", "Catalyst Support");
                helper.setTo(email);

                String subject = "Here's the link to reset your password";
                helper.setSubject(subject);

                String content = "<p>Hello,</p>" +
                        "<p>We received a request to reset the password for your account.</p>" +
                        "<p>Click the link below to reset your password:</p>" +
                        "<p><a href=\"" + resetPasswordLink + "\">Reset your password</a></p>" +
                        "<p><strong>This link will expire in 15 minutes</strong> for your security.</p>" +
                        "<p>If you didn't request a password reset, please ignore this email.</p>" +
                        "<p>For security reasons, we recommend:</p>" +
                        "<ul>" +
                        "<li>Not sharing this link with anyone</li>" +
                        "<li>Creating a strong, unique password</li>" +
                        "</ul>" +
                        "<p>Best regards,<br/>Catalyst Support</p>";

                helper.setText(content, true);
                mailSender.send(message);

                System.out.println("Password reset email sent to: " + email);
            }
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingException("Error occurred while preparing the email content (encoding issue).");
        } catch (MessagingException e) {
            throw new MessagingException("Error occurred while sending the password reset email.");
        }
    }
}
