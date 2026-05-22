# Job Recommendation Email System - DB Backed Queue Architecture

## Goal

Implement a scalable scheduled email recommendation system in a monolithic Spring Boot application using:

- Scheduler
- DB-backed queue
- Batch user processing
- Per-user email task retry
- Concurrent workers
- Transaction-safe processing

---

# High Level Architecture

```text
Scheduler
    ↓
Load subscribed users by batch
    ↓
Create email tasks in DB queue

Worker Threads
    ↓
Poll pending tasks
    ↓
Load user subscriber on skills + recommended jobs
    ↓
Send email
    ↓
Update task status
```

---

# Requirements

## Functional Requirements

- Send job recommendation emails every day at 8:00 AM
- Only send to users who already have created subscriber
- Retry failed emails
- Prevent duplicate email sending
- Support concurrent processing
- Support future scaling

---

# Database Design

## email_task table

```sql
CREATE TABLE email_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,

    status VARCHAR(20) NOT NULL,
    retry_count INT NOT NULL DEFAULT 0,

    next_retry_at TIMESTAMP NULL,

    error_message TEXT NULL,

    send_date DATE NOT NULL,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

---

# Recommended Status Values

```text
PENDING
PROCESSING
SUCCESS
FAILED
```

---

# Important Constraints

Prevent duplicate tasks:

```sql
UNIQUE(user_id, send_date)
```

---

# Recommended Indexes

```sql
CREATE INDEX idx_email_task_status_retry
ON email_task(status, next_retry_at);
```

---

# Scheduler Responsibilities

## Scheduler MUST NOT send emails directly.

Scheduler only:

1. Load subscribed users by batch
2. Create email tasks

---

# Scheduler Logic

## Run every day at 8:00 AM

```java
@Scheduled(cron = "0 0 8 * * *")
```

---

# Batch Processing

DO NOT:

```java
findAll()
```

USE:

```java
PageRequest.of(page, 100)
```

Recommended batch size:

```text
100 - 500
```

---

# Scheduler Flow

```text
Loop through subscribed users by page
    ↓
Create email task for each user
    ↓
Ignore duplicates if task already exists
```

---

# Worker Responsibilities

Workers process tasks independently.

Each task represents ONE USER EMAIL.

---

# Worker Polling Strategy

Workers continuously poll pending tasks:

```sql
SELECT *
FROM email_task
WHERE status = 'PENDING'
AND (
    next_retry_at IS NULL
    OR next_retry_at <= NOW()
)
LIMIT 10
FOR UPDATE SKIP LOCKED
```

---

# Why FOR UPDATE SKIP LOCKED?

Prevents multiple workers from processing the same task simultaneously.

Required for concurrency safety.

---

# Email Processing Flow

```text
Claim task
    ↓
Load user
    ↓
Load subscriptions
    ↓
Load recommended jobs
    ↓
Generate email content
    ↓
Send email
    ↓
Update task status
```

---

# Transaction Rules

## IMPORTANT

DO NOT wrap the whole email sending flow inside one transaction.

BAD:

```java
@Transactional
public void processTask() {
    loadData();
    sendEmail();
    updateStatus();
}
```

Reason:

- Holds DB connection too long
- External I/O inside transaction
- Poor throughput
- Risk of connection pool exhaustion

---

# Correct Transaction Boundary

Transactions should only wrap:

- task claiming
- status updates
- logging

Transactions must remain SHORT.

---

# Retry Strategy

Retry PER EMAIL TASK.

DO NOT retry entire batch.

---

# Retry Flow

If email sending fails:

```text
retry_count += 1
status = PENDING
next_retry_at = future timestamp
```

---

# Example Retry Delays

```text
Retry 1 -> +1 minute
Retry 2 -> +5 minutes
Retry 3 -> +15 minutes
Retry 4 -> +1 hour
```

---

# Max Retry

Recommended:

```text
max_retry = 5
```

If exceeded:

```text
status = FAILED
```

---

# Concurrency

Use worker thread pool.

Example:

```java
ExecutorService fixedThreadPool(5)
```

Recommended worker count:

```text
5 - 10 workers
```

depending on:

- DB capacity
- SMTP provider rate limit
- CPU usage

---

# Memory Control

Never load all users into memory.

Always process using pagination/batching.

Purpose:

- avoid OOM
- reduce GC pressure
- stable memory usage

---

# Idempotency

Prevent duplicate email sending.

Recommended:

```sql
UNIQUE(user_id, send_date)
```

or store:

```text
email_request_id
```

---

# Logging

Log:

- total tasks created
- success count
- failed count
- retry count
- processing duration

---

# Cleanup Strategy

Archive or delete old SUCCESS tasks periodically.

Example:

```text
Delete SUCCESS tasks older than 30 days
```

---

# Recommended Spring Components

## Scheduler

```text
@Scheduled
```

---

## Async Workers

```text
ExecutorService
or
@Async
```

---

## Persistence

```text
Spring Data JPA
Hibernate
```

---

# Important Design Principles

## Batch is ONLY for:

- pagination
- memory control

NOT transaction boundary.

---

## Retry boundary = one email task

NOT entire batch.

---

## Keep transactions short

Never include:

- email sending
- external API calls
- retry waiting
- thread sleep

inside transactions.

---

# Final Architecture Summary

```text
Scheduler
    ↓
Create email tasks in DB

Workers
    ↓
Poll pending tasks
    ↓
Process user independently
    ↓
Retry failed tasks
    ↓
Update status
```

