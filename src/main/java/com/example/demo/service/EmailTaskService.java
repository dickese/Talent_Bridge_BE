package com.example.demo.service;

import com.example.demo.model.persistence.emailTask.EmailTask;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmailTaskService {
    /**
     * Create an email task batch
     */
    List<EmailTask> createBatchEmailTask(List<String> emails);
    
    /**
     * Fetch pending tasks AND immediately claim them (atomically) in a single transaction
     *
     * This solves the race condition where:
     * - Thread-1 fetches Task-A with FOR UPDATE lock
     * - Lock is released when transaction ends
     * - Thread-2 can fetch same Task-A before Thread-1 claims it
     *
     * Solution: Fetch + Claim (update status to PROCESSING) within a single transaction
     * This ensures the lock is held from SELECT to UPDATE, preventing duplicate processing.
     *
     * @param limit number of tasks to fetch and claim
     * @return tasks that have been claimed (status = PROCESSING)
     */
    List<EmailTask> fetchAndClaimPendingTasks(int limit);

    List<EmailTask> updateTaskResultInBatch(List<EmailTask> tasks);

    /**
     * Mark task as failed with error message
     * If retry count < max, schedule next retry
     * Otherwise, set status to FAILED
     */
    EmailTask markAsFailed(EmailTask task, String errorMessage);
    EmailTask skipTaskAsFailed(EmailTask task, String errorMessage);


    /**
     * Get task statistics for a date
     */
    EmailTaskStatistics getStatistics(LocalDate date);
    
    /**
     * Clean up old successful tasks
     */
    void cleanupOldSuccessfulTasks(LocalDate beforeDate);
}

