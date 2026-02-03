package com.user.usermanagement.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
@Service
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    // Constructor Injection
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendWelcomeEmail(String toEmail, String name) {
        log.info("Starting Async email task for: {}", toEmail);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Welcome to Our Platform!");

            // Setting up a simple HTML body
            String htmlContent = "<h3>Hello " + name + ",</h3>" +
                    "<p>Your account has been successfully created.</p>" +
                    "<p>Happy Coding!</p>";

            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Email successfully sent to: {}", toEmail);

        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
        }
    }
}
