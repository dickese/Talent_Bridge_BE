package com.example.demo.service.impl;

import com.example.demo.model.domain.job.Job;
import com.example.demo.model.domain.subscriber.Subscriber;
import com.example.demo.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
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

    @Override
    public void sendJobRecommendationEmail(Subscriber subscriber, List<Job> recommendedJobs) {
        if (subscriber.getEmail() == null || subscriber.getEmail().isBlank()) {
            log.warn("Subscriber has no email: {}", subscriber.getId());
            throw new RuntimeException("Subscriber email is required");
        }

        validateEmail(subscriber.getEmail());

        String htmlContent = buildJobRecommendationEmail(subscriber, recommendedJobs);

        try {
            String subject = "Job Recommendations - " + recommendedJobs.size() + " matching jobs";
            sendHtmlEmail(subscriber.getEmail(), subject, htmlContent);
            log.info("Sent job recommendation email to: {}", subscriber.getEmail());
        } catch (MessagingException e) {
            log.error("Failed to send job recommendation email to: {}", subscriber.getEmail(), e);
            throw new RuntimeException("Email sending failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendTestEmail() {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo("test@gmail.com");
        message.setSubject("Test MailHog");
        message.setText("Hello from Spring Boot");

        mailSender.send(message);
    }

    private String buildVerifyEmail(String name, String verifyUrl){
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("verifyUrl", verifyUrl);
        context.setVariable("expiredMinutes", 15);

        return templateEngine.process("verify-email", context);
    }

    private String buildJobRecommendationEmail(Subscriber subscriber, List<Job> recommendedJobs) {
        Context context = new Context();
        context.setVariable("email", subscriber.getEmail());
        context.setVariable("experienceLevel", subscriber.getLevel() != null ? subscriber.getLevel().toString() : "N/A");
        context.setVariable("expectedSalary", subscriber.getExpectedSalary());
        context.setVariable("recommendedJobs", recommendedJobs);
        context.setVariable("jobCount", recommendedJobs.size());

        return templateEngine.process("job-recommendation-email", context);
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
