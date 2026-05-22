package com.example.demo.scheduler;

import com.example.demo.model.persistence.emailTask.EmailTask;
import com.example.demo.model.persistence.emailTask.EmailTaskStatus;
import com.example.demo.repository.SubscriberRepository;
import com.example.demo.scheduler.worker.EmailTaskWorkerPool;
import com.example.demo.service.EmailTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobRecommendationScheduler {

    private final SubscriberRepository subscriberRepository;
    private final EmailTaskService emailTaskService;
    private final EmailTaskWorkerPool emailTaskWorkerPool;
    private static final int BATCH_SIZE = 100;

    /**
     * "0 0 8 * * *" = 8:00 AM every day
     */
    @Scheduled(cron = "0 0 8 * * *")
    @Transactional
    public void scheduleJobRecommendationEmails() {
        log.info("Starting job recommendation email scheduler");

        Instant startTime = Instant.now();

        try {
            // Ensure worker pool is running
            if (!emailTaskWorkerPool.isRunning()) {
                log.info("Starting email task worker pool");
                emailTaskWorkerPool.start();
            }

            long totalCreated = 0;
            int pageNumber = 0;

            while (true) {
                // Process each email of subscriber in batch
                Pageable pageable = PageRequest.of(0, BATCH_SIZE);
                Page<String> page = subscriberRepository.findEmailSubscribersNotSentToday(pageable);

                if (page.isEmpty()) {
                    log.info("Finished processing subscribers at page {}", pageNumber);
                    break;
                }

                log.info("Processing batch {} with {} subscribers", pageNumber, page.getNumberOfElements());
                totalCreated += emailTaskService.createBatchEmailTask(page.getContent()).size();

                if (!page.hasNext()) {
                    break;
                }
                pageNumber++;
            }

            long duration = java.time.Duration.between(startTime, Instant.now()).getSeconds();

            log.info("Job recommendation scheduler completed. Created: {}, Duration: {} seconds",
                totalCreated, duration);


        } catch (Exception e) {
            log.error("Error in job recommendation scheduler", e);
            throw new RuntimeException("Scheduler failed: " + e.getMessage(), e);
        }
    }
}
