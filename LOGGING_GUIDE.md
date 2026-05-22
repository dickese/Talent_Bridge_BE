# Email Recommendation Flow - Logging Guide

## Overview

Hệ thống logging đã được cấu hình để ghi tất cả logs liên quan đến flow gửi email job recommendation vào **một file riêng**: `logs/email-recommendation-flow.log`

---

## 📁 File Logs

### 1. Email Recommendation Flow Log
- **Đường dẫn**: `logs/email-recommendation-flow.log`
- **Nội dung**: Tất cả logs liên quan đến:
  - JobRecommendationScheduler
  - EmailTaskWorker
  - EmailTaskWorkerPool
  - EmailTaskService
  - EmailService
  - JobService (job matching)

### 2. General Application Log
- **Đường dẫn**: `logs/application.log`
- **Nội dung**: Tất cả logs khác của ứng dụng

### 3. Archived Logs
- **Format**: `email-recommendation-flow-%d{yyyy-MM-dd}.%i.log.gz`
- **Rotation**: 
  - Daily rotation
  - Khi file vượt quá 100MB
  - Giữ tối đa 30 ngày
  - Tổng kích thước tối đa: 3GB

---

## 🔧 Cấu Hình Chi Tiết

### logback-spring.xml

File cấu hình chính nằm tại:
```
src/main/resources/logback-spring.xml
```

**Các thành phần chính:**

1. **EMAIL_FLOW_FILE Appender**
   - Ghi logs vào `logs/email-recommendation-flow.log`
   - Rolling policy: Daily + Size-based rotation
   - Giữ 30 ngày lịch sử, tối đa 3GB

2. **ASYNC_EMAIL_FLOW Appender**
   - Wrapper async cho EMAIL_FLOW_FILE
   - Queue size: 512
   - Không discard logs
   - Tăng performance

3. **Logger Configuration**
   - 6 loggers chuyên biệt cho email flow components
   - Mức log: DEBUG
   - Additivity: false (không inherit parent logger)

### application.yaml

Cấu hình logging được thêm vào:
```yaml
logging:
  level:
    root: ${LOG_LEVEL:INFO}
    com.example.demo.scheduler: ${EMAIL_SCHEDULER_LOG_LEVEL:DEBUG}
    com.example.demo.scheduler.worker: ${EMAIL_WORKER_LOG_LEVEL:DEBUG}
    # ... các loggers khác
  file:
    name: ${LOG_FILE_PATH:logs/application.log}
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
```

---

## 📊 Log Output Format

Mỗi log line có format:
```
2026-05-21 08:00:00.123 [EmailTaskWorker-1] DEBUG com.example.demo.scheduler.JobRecommendationScheduler - Starting job recommendation email scheduler
```

**Thành phần:**
- **Timestamp**: `2026-05-21 08:00:00.123` - Thời gian chính xác đến millisecond
- **Thread**: `[EmailTaskWorker-1]` - Thread name
- **Level**: `DEBUG` - Log level (DEBUG, INFO, WARN, ERROR)
- **Logger**: `com.example.demo.scheduler.JobRecommendationScheduler` - Class name
- **Message**: Log message

---

## 🚀 Sử Dụng

### 1. Khởi động ứng dụng
```bash
mvn spring-boot:run
```

Logs sẽ tự động được ghi vào:
- Console (real-time viewing)
- `logs/email-recommendation-flow.log` (email flow logs)
- `logs/application.log` (general logs)

### 2. Xem logs real-time
```bash
# Unix/Linux/Mac
tail -f logs/email-recommendation-flow.log

# Windows PowerShell
Get-Content logs/email-recommendation-flow.log -Tail 50 -Wait
```

### 3. Tìm kiếm logs
```bash
# Unix/Linux/Mac
grep "ERROR\|WARN" logs/email-recommendation-flow.log

# Windows PowerShell
Select-String "ERROR|WARN" logs/email-recommendation-flow.log
```

### 4. Xem logs kỳ trước
```bash
# Archived logs (gzip)
zcat logs/email-recommendation-flow-2026-05-20.1.log.gz | grep "ERROR"
```

---

## 🎯 Các Components Được Log

### 1. JobRecommendationScheduler
```log
Starting job recommendation email scheduler
Processing batch 0 with 100 subscribers
Created email task for userId: 123, taskId: 456
Finished processing subscribers at page 2
Job recommendation scheduler completed. Created: 200, Skipped: 5, Duration: 12 seconds
```

### 2. EmailTaskWorkerPool
```log
Starting email task worker pool with 5 workers
Email task worker pool started successfully
Fetched 10 pending tasks for processing
No tasks for 60000 ms, initiating auto-shutdown of worker pool
Worker thread EmailTaskWorker-1 exiting
```

### 3. EmailTaskWorker
```log
Processing email task 123
Found 8 recommended jobs for subscriber: 567
Successfully processed email task 123
Error processing email task 123: SMTP connection failed
```

### 4. EmailTaskService
```log
Created email task for userId: 123, taskId: 456
Claimed task 456
Task 456 marked as SUCCESS
Task 456 scheduled for retry #2 at 2026-05-21T14:32:00Z
Task 456 failed after 5 retries. Error: Permanent SMTP failure
```

### 5. EmailService
```log
Sending job recommendation email to subscriber: example@gmail.com
Successfully sent email to subscriber: example@gmail.com
Failed to send email to subscriber: example@gmail.com
```

### 6. JobService
```log
Finding jobs matching subscriber: 567
Job matching completed. Found: 8 jobs matching subscriber skills
```

---

## 🔍 Monitoring

### Daily Scheduler Execution
Mỗi ngày lúc 8:00 AM, bạn sẽ thấy:
```log
2026-05-21 08:00:00.123 [scheduler-1] INFO ... Starting job recommendation email scheduler
2026-05-21 08:00:00.456 [scheduler-1] INFO ... Processing batch 0 with 100 subscribers
2026-05-21 08:00:05.789 [scheduler-1] INFO ... Job recommendation scheduler completed. Created: 150, Skipped: 5, Duration: 5 seconds
```

### Worker Processing
Logs từ worker threads (liên tục):
```log
2026-05-21 08:00:15.123 [EmailTaskWorker-1] INFO ... Processing email task 1001
2026-05-21 08:00:17.456 [EmailTaskWorker-1] INFO ... Successfully processed email task 1001
2026-05-21 08:00:18.789 [EmailTaskWorker-2] INFO ... Processing email task 1002
```

### Error Tracking
Các lỗi sẽ được ghi chi tiết:
```log
2026-05-21 08:00:20.123 [EmailTaskWorker-1] ERROR ... Error processing email task 1003: SMTP connection timeout
2026-05-21 08:00:20.456 [EmailTaskWorker-1] INFO ... Task 1003 scheduled for retry #1 at 2026-05-21T08:01:20Z
```

---

## ⚙️ Environment Variables

Có thể thay đổi behavior logging thông qua environment variables:

```bash
# Log level configuration
export LOG_LEVEL=DEBUG                              # Root logger level
export EMAIL_SCHEDULER_LOG_LEVEL=DEBUG              # Scheduler level
export EMAIL_WORKER_LOG_LEVEL=DEBUG                 # Worker level
export EMAIL_TASK_SERVICE_LOG_LEVEL=DEBUG           # EmailTaskService level
export EMAIL_SERVICE_LOG_LEVEL=DEBUG                # EmailService level
export EMAIL_JOB_SERVICE_LOG_LEVEL=DEBUG            # JobService level

# Log file location
export LOG_FILE_PATH=logs/application.log            # General log file path
```

---

## 📝 Log Levels

| Level | Sử dụng khi | Ví dụ |
|-------|-----------|------|
| **ERROR** | Lỗi nghiêm trọng | SMTP connection failed, Subscriber not found |
| **WARN** | Cảnh báo | Task already exists (duplicate) |
| **INFO** | Thông tin quan trọng | Task created, Scheduler started, Email sent |
| **DEBUG** | Chi tiết debugging | Task claimed, User loaded, Jobs found |
| **TRACE** | Quá chi tiết (hiếm khi dùng) | Không được sử dụng |

---

## 🛠️ Troubleshooting

### Logs không xuất hiện

1. **Kiểm tra logs directory tồn tại**
   ```bash
   ls -la logs/  # Unix/Linux/Mac
   dir logs\     # Windows
   ```

2. **Kiểm tra permissions**
   - Ứng dụng phải có quyền write vào `logs/` directory

3. **Kiểm tra logback configuration**
   - File `logback-spring.xml` phải nằm ở: `src/main/resources/`

### Logs quá nhiều dung lượng

1. **Giảm log level**
   ```yaml
   logging:
     level:
       root: INFO  # Từ DEBUG xuống INFO
   ```

2. **Giảm lịch sử giữ lại**
   ```xml
   <maxHistory>14</maxHistory>  # Từ 30 ngày xuống 14 ngày
   ```

3. **Giảm size tối đa**
   ```xml
   <totalSizeCap>1GB</totalSizeCap>  # Từ 3GB xuống 1GB
   ```

### Chỉ muốn xem ERROR logs

```yaml
logging:
  level:
    com.example.demo.scheduler: ERROR
    com.example.demo.scheduler.worker: ERROR
```

---

## 📚 Profile-Specific Configuration

### Development Profile (`dev`)
```bash
export SPRING_PROFILES_ACTIVE=dev
```
- Log level: DEBUG
- Output: Console + File
- Giữ chi tiết cho debugging

### Production Profile (`prod`)
```bash
export SPRING_PROFILES_ACTIVE=prod
```
- Log level: INFO
- Output: File only (không console)
- Giảm I/O overhead

---

## 🔄 Log Rotation Details

### Trigger Rotation

Logs sẽ rotate khi:
1. **Daily**: Mỗi ngày lúc 00:00
2. **Size-based**: Khi file vượt quá 100MB

### Archive Format

Old logs được compress:
- `email-recommendation-flow-2026-05-20.1.log.gz`
- `email-recommendation-flow-2026-05-20.2.log.gz` (nếu có nhiều file cùng ngày)

### Retention Policy

- **Max history**: 30 ngày
- **Total size cap**: 3GB
- Logs cũ hơn 30 ngày sẽ bị xóa tự động

---

## 📞 Support

Để thay đổi cấu hình logging:

1. **Cập nhật `logback-spring.xml`** cho logging format/appenders
2. **Cập nhật `application.yaml`** cho log levels
3. **Sử dụng environment variables** cho configuration động

Restart ứng dụng để áp dụng thay đổi.

---

## 📖 References

- Logback Documentation: https://logback.qos.ch/
- Spring Boot Logging: https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging
- Slf4j: https://www.slf4j.org/


