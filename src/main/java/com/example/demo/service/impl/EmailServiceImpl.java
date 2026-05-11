package com.example.demo.service.impl;

import com.example.demo.model.persistence.emailToken.EmailVerificationToken;
import com.example.demo.model.domain.user.User;
import com.example.demo.repository.VerificationTokenRepository;
import com.example.demo.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendVerifyEmail(String to, String name, String verifyUrl) {
        validateEmail(to);

        String htmlContent = buildVerifyEmail(name, verifyUrl);

        try {
            String subjectEmail = "Verify your email";
            sendHtmlEmail(to, subjectEmail, htmlContent);
        }catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildVerifyEmail(String name, String verifyUrl){
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("verifyUrl", verifyUrl);
        context.setVariable("expiredMinutes", 15);

        return templateEngine.process("verify-email", context);
    }

    private void sendHtmlEmail(String toEmail, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    private void validateEmail(String to){
//        if(!EmailValidator.getInstance().isValid(to)){
//            throw new IllegalArgumentException("Invalid email");
//        }
    }

}
