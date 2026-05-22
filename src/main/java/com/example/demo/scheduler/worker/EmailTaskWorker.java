package com.example.demo.scheduler.worker;

import com.example.demo.model.domain.job.Job;
import com.example.demo.model.domain.subscriber.Subscriber;
import com.example.demo.model.domain.user.User;
import com.example.demo.model.persistence.emailTask.EmailTask;
import com.example.demo.repository.SubscriberRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.EmailTaskService;
import com.example.demo.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailTaskWorker {

    private final EmailTaskService emailTaskService;
    private final SubscriberRepository subscriberRepository;
    private final JobService jobService;
    private final EmailService emailService;

    public boolean processTask(EmailTask task) {
        try {
            log.info("Processing email task {}", task.getId());
            // Load subscriber data (separate transaction)
            Subscriber subscriber = subscriberRepository.findByEmail(task.getEmail())
                .orElseThrow(() -> new RuntimeException("Subscriber not found for email: " + task.getEmail()));

            // Load recommended jobs based on subscriber's skills and preferences
            List<Job> recommendedJobs = jobService.getJobsMatchingSubscriber(subscriber);

            if (recommendedJobs.isEmpty()) {
                log.info("No recommended jobs found for subscriber: {}", subscriber.getId());
                emailTaskService.skipTaskAsFailed(task, "No recommended jobs found for subscriber");
                return false;
            } else {
                log.info("Found {} recommended jobs for subscriber: {}", recommendedJobs.size(), subscriber.getId());
            }

            // Send email (external I/O - NOT in transaction)
            emailService.sendJobRecommendationEmail(subscriber, recommendedJobs);

            task.markAsSuccess();
            log.info("Successfully processed email task {}", task.getId());
            return true;
        } catch (Exception e) {
            log.error("Error processing email task {}: {}", task.getId(), e.getMessage(), e);
            try {
                emailTaskService.markAsFailed(task, e.getMessage());
            } catch (Exception retryError) {
                log.error("Failed to update task status for task {}", task.getId(), retryError);
            }
        }

        return false;
    }
}

