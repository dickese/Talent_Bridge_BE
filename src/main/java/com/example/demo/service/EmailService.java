package com.example.demo.service;

import com.example.demo.model.domain.job.Job;
import com.example.demo.model.domain.subscriber.Subscriber;
import com.example.demo.model.domain.user.User;

import java.util.List;

public interface EmailService {
    void sendVerifyEmail(String to, String name, String verifyUrl);

    void sendTestEmail();
    /**
     * Send job recommendation email to a subscriber
     * @param subscriber The subscriber who will receive the email
     * @param recommendedJobs List of recommended jobs
     * @throws RuntimeException if email sending fails
     */
    void sendJobRecommendationEmail(Subscriber subscriber, List<Job> recommendedJobs);
}
