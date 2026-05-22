package com.example.demo.scheduler.worker;

import com.example.demo.model.persistence.emailTask.EmailTask;
import com.example.demo.service.EmailTaskService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages a pool of worker threads that process email tasks concurrently
 *
 * Features:
 * - Fixed thread pool (configurable, default 5-10 workers)
 * - Continuous polling for pending tasks
 * - Graceful shutdown
 * - Thread-safe task processing
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmailTaskWorkerPool {

    private final EmailTaskService emailTaskService;
    private final EmailTaskWorker emailTaskWorker;

    @Value("${email.worker.pool-size:5}")
    private int poolSize;

    @Value("${email.worker.poll-limit:10}")
    private int pollLimit;

    @Value("${email.worker.poll-interval-ms:5000}")
    private long pollIntervalMs;

    @Value("${email.worker.max-idle-ms:60000}")
    private long maxIdleMs;

    private ExecutorService executorService;
    /**
     * -- GETTER --
     *  Check if worker pool is running
     */
    @Getter
    private volatile boolean running = false;

    // last time any worker found tasks or processed tasks
    private volatile long lastActiveAt = System.currentTimeMillis();

    // ensure only one thread triggers auto-shutdown
    private final java.util.concurrent.atomic.AtomicBoolean shutdownInitiated = new java.util.concurrent.atomic.AtomicBoolean(false);

    private Instant startTime;

    public synchronized void start() {
        if (running) {
            log.warn("Worker pool is already running");
            return;
        }

        log.info("Starting email task worker pool with {} workers", poolSize);

        // Create thread pool with named threads for debugging
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger count = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("EmailTaskWorker-" + count.incrementAndGet());
                t.setDaemon(false);
                return t;
            }
        };

        executorService = Executors.newFixedThreadPool(poolSize, threadFactory);
        running = true;
        startTime = Instant.now();
        // reset activity markers
        lastActiveAt = System.currentTimeMillis();
        shutdownInitiated.set(false);

        for (int i = 0; i < poolSize; i++) {
            executorService.submit(this::pollAndProcessTasks);
        }

        log.info("Email task worker pool started successfully");
    }

    public synchronized void shutdown() {
        if (!running) {
            log.warn("Worker pool is not running");
            return;
        }

        log.info("Stopping email task worker pool");
        running = false;

        if (executorService != null) {
            executorService.shutdown();
            log.info("Worker pool shutdown initiated");
        }
    }

    private void pollAndProcessTasks() {
        while (running) {
            try {
//                fetch PENDING tasks and marked as PROCESSING in single transaction to avoid race-condition
                List<EmailTask> pendingTasks = emailTaskService.fetchAndClaimPendingTasks(pollLimit);

                if (pendingTasks.isEmpty()) {
                    long idleMs = System.currentTimeMillis() - lastActiveAt;
                    if (idleMs >= maxIdleMs) {
                        if (shutdownInitiated.compareAndSet(false, true)) {
                            log.info("No tasks for {} ms, initiating auto-shutdown of worker pool", idleMs);
                            try {
                                var resultStatistic = emailTaskService.getStatistics(LocalDate.now());
                                log.info("Total tasks: {}", resultStatistic.getTotalTasks());
                                log.info("Successful tasks: {}", resultStatistic.getSuccessCount());
                                log.info("Failed tasks: {}", resultStatistic.getFailedCount());
                                log.info("Pending tasks: {}", resultStatistic.getPendingCount());
                                Duration sec = Duration.between(startTime, LocalDate.now());
                                log.info("Process {} tasks in {}s", resultStatistic.getTotalTasks(), sec.getSeconds());
                                shutdown();
                            } catch (Exception e) {
                                log.error("Error during auto-shutdown", e);
                            }
                        }
                        break;
                    }

                    // No tasks available, wait before polling again
                    Thread.sleep(pollIntervalMs);
                    continue;
                }

                // reset idle marker when tasks are found
                lastActiveAt = System.currentTimeMillis();

                log.debug("Fetched and claimed {} pending tasks for processing", pendingTasks.size());

                // Process each task
                for (EmailTask task : pendingTasks) {
                    try {
                        emailTaskWorker.processTask(task);
                    } catch (Exception e) {
                        log.error("Unexpected error processing task {}", task.getId(), e);
                    }
                }

                emailTaskService.updateTaskResultInBatch(pendingTasks);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("Worker thread interrupted");
                break;
            } catch (Exception e) {
                log.error("Error in poll and process loop", e);
                try {
                    Thread.sleep(pollIntervalMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        log.info("Worker thread {} exiting", Thread.currentThread().getName());
    }

}

