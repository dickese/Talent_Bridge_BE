package com.example.demo.service.impl;

import com.example.demo.model.persistence.emailTask.EmailTask;
import com.example.demo.model.persistence.emailTask.EmailTaskStatus;
import com.example.demo.repository.EmailTaskRepository;
import com.example.demo.service.EmailTaskService;
import com.example.demo.service.EmailTaskStatistics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailTaskServiceImpl implements EmailTaskService {

    private final EmailTaskRepository emailTaskRepository;

    @Value("${email.task.max-retry:5}")
    private int maxRetry;

    @Override
    @Transactional
    public List<EmailTask> createBatchEmailTask(List<String> emails) {
        List<EmailTask> tasks =  emails.stream().map(this::buildEmailTask).toList();
        return emailTaskRepository.saveAll(tasks);
    }

    private EmailTask buildEmailTask(String email){
        LocalDate sendDate = LocalDate.now();
        return EmailTask.builder()
                .email(email)
                .sendDate(sendDate)
                .status(EmailTaskStatus.PENDING)
                .retryCount(0)
                .nextRetryAt(null)
                .build();
    }

    @Override
    @Transactional
    public List<EmailTask> fetchAndClaimPendingTasks(int limit) {
        List<EmailTask> claimedTasks = emailTaskRepository.fetchAndClaimPendingTasks(limit);
        log.debug("Fetched and claimed {} tasks for processing", claimedTasks.size());
        return claimedTasks;
    }

    @Override
    @Transactional
    public List<EmailTask> updateTaskResultInBatch(List<EmailTask> tasks) {
        return emailTaskRepository.saveAll(tasks);
    }

    @Override
    public EmailTask markAsFailed(EmailTask task, String errorMessage) {
        task.setErrorMessage(errorMessage);
        task.setRetryCount(task.getRetryCount() + 1);

        if (task.getRetryCount() >= maxRetry) {
            // Max retries exceeded, mark as FAILED
            task.setStatus(EmailTaskStatus.FAILED);
            log.warn("Task {} failed after {} retries. Error: {}", task.getId(), maxRetry, errorMessage);
        } else {
            // Schedule next retry
            task.setStatus(EmailTaskStatus.PENDING);
            task.setNextRetryAt(calculateNextRetryTime(task.getRetryCount()));
            log.info("Task {} scheduled for retry #{} at {}",  task.getId(), task.getRetryCount(), task.getNextRetryAt());
        }

        return task;
    }

    @Override
    public EmailTask skipTaskAsFailed(EmailTask task, String errorMessage) {
        task.setErrorMessage(errorMessage);
        task.setStatus(EmailTaskStatus.FAILED);
        return task;
    }

    @Override
    @Transactional(readOnly = true)
    public EmailTaskStatistics getStatistics(LocalDate date) {
        long totalTasks = emailTaskRepository.countTasksByDate(date);
        long successCount = emailTaskRepository.countSuccessByDate(date);
        long failedCount = emailTaskRepository.countFailedByDate(date);
        long pendingCount = emailTaskRepository.countPendingAndProcessingByDate(date);

        return EmailTaskStatistics.builder()
            .totalTasks(totalTasks)
            .successCount(successCount)
            .failedCount(failedCount)
            .pendingCount(pendingCount)
            .build();
    }

    @Override
    @Transactional
    public void cleanupOldSuccessfulTasks(LocalDate beforeDate) {
        // Implementation for cleanup - can be extended if needed
        log.info("Cleanup requested for tasks before {}", beforeDate);
    }

    /**
     * Calculate next retry time with exponential backoff
     * Retry 1 -> +1 minute
     * Retry 2 -> +5 minutes
     * Retry 3 -> +15 minutes
     * Retry 4 -> +1 hour
     * Retry 5+ -> +2 hours
     */
    private Instant calculateNextRetryTime(int retryCount) {
        return switch (retryCount) {
            case 1 -> Instant.now().plus(1, ChronoUnit.MINUTES);
            case 2 -> Instant.now().plus(5, ChronoUnit.MINUTES);
            case 3 -> Instant.now().plus(15, ChronoUnit.MINUTES);
            case 4 -> Instant.now().plus(1, ChronoUnit.HOURS);
            default -> Instant.now().plus(2, ChronoUnit.HOURS);
        };
    }
}

