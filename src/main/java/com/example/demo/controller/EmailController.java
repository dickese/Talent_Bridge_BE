package com.example.demo.controller;

import com.example.demo.scheduler.JobRecommendationScheduler;
import com.example.demo.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;
    private final JobRecommendationScheduler jobRecommendationScheduler;
    @GetMapping("/test")
    public void send() {

        emailService.sendTestEmail();
    }

    @GetMapping("/schedule")
    public void triggerEmailSenderScheduler() {
        jobRecommendationScheduler.scheduleJobRecommendationEmails();
    }
}
