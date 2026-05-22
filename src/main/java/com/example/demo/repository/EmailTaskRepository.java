package com.example.demo.repository;

import com.example.demo.model.persistence.emailTask.EmailTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmailTaskRepository extends JpaRepository<EmailTask, Long> {

    /**
     * Fetch pending tasks AND immediately claim them (update status to PROCESSING) in a single transaction
     * This prevents race conditions where:
     * - Thread-1 fetches Task-A and releases lock
     * - Thread-2 fetches same Task-A before Thread-1 can claim it
     *
     * Solution: Atomically fetch + claim in one transaction to hold the lock
     *
     * @param limit number of tasks to fetch
     * @return tasks that are now PROCESSING
     */
    @Modifying
    @Query(value = """
        UPDATE email_task
        SET status = 'PROCESSING', updated_at = CURRENT_TIMESTAMP
        WHERE id IN (
            SELECT id FROM email_task
            WHERE status = 'PENDING'
            AND (next_retry_at IS NULL OR next_retry_at <= CURRENT_TIMESTAMP)
            LIMIT :limit
            FOR UPDATE
            SKIP LOCKED
        )
        RETURNING *
        """, nativeQuery = true)
    List<EmailTask> fetchAndClaimPendingTasks(@Param("limit") int limit);

    /**
     * Count total tasks created on a specific date
     */
    @Query("SELECT COUNT(t) FROM EmailTask t WHERE t.sendDate = :sendDate")
    long countTasksByDate(@Param("sendDate") LocalDate sendDate);

    /**
     * Count successful tasks
     */
    @Query("SELECT COUNT(t) FROM EmailTask t WHERE t.status = 'SUCCESS' AND t.sendDate = :sendDate")
    long countSuccessByDate(@Param("sendDate") LocalDate sendDate);

    /**
     * Count failed tasks
     */
    @Query("SELECT COUNT(t) FROM EmailTask t WHERE t.status = 'FAILED' AND t.sendDate = :sendDate")
    long countFailedByDate(@Param("sendDate") LocalDate sendDate);

    /**
     * Count pending and processing tasks
     */
    @Query("SELECT COUNT(t) FROM EmailTask t WHERE (t.status = 'PENDING' OR t.status = 'PROCESSING') AND t.sendDate = :sendDate")
    long countPendingAndProcessingByDate(@Param("sendDate") LocalDate sendDate);

    Optional<EmailTask> findByEmailAndSendDate(String email, LocalDate sendDate);
}

