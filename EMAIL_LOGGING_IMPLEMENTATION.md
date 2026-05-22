# Email Recommendation Flow - Logging Implementation Summary

## 📋 Tóm Tắt Thay Đổi

Đã cấu hình logging để ghi **toàn bộ flow gửi email job recommendation** vào **một file riêng**: `logs/email-recommendation-flow.log`

---

## 📁 Files Tạo Mới / Cập Nhật

### ✨ **NEW FILES**

1. **`src/main/resources/logback-spring.xml`** (140 lines)
   - Cấu hình Logback XML cho Spring Boot
   - Định nghĩa 2 appenders:
     - `EMAIL_FLOW_FILE`: Ghi vào `logs/email-recommendation-flow.log`
     - `FILE`: Ghi vào `logs/application.log`
   - Cấu hình 6 loggers chuyên biệt cho email flow components
   - Rolling policy: Daily + 100MB per day, keep 30 days, max 3GB total
   - Async appender cho performance tối ưu
   - Profile-specific config (dev vs prod)

2. **`LOGGING_GUIDE.md`** (300+ lines)
   - Hướng dẫn chi tiết toàn diện
   - Components được log
   - Monitoring, troubleshooting, configuration
   - Environment variables usage
   - Docker/Kubernetes examples

3. **`EMAIL_LOGGING_QUICK_START.md`** (200+ lines)
   - Quick start guide cho người mới
   - Verification checklist
   - Expected log output examples
   - Troubleshooting tips

4. **`.env.logging.example`**
   - Example environment variables
   - Configuration presets (dev, prod, debug)
   - Docker & Kubernetes examples

### 📝 **UPDATED FILES**

1. **`src/main/resources/application.yaml`**
   - Thêm section `logging:` với cấu hình:
     ```yaml
     logging:
       level:
         root: ${LOG_LEVEL:INFO}
         com.example.demo.scheduler: ${EMAIL_SCHEDULER_LOG_LEVEL:DEBUG}
         com.example.demo.scheduler.worker: ${EMAIL_WORKER_LOG_LEVEL:DEBUG}
         com.example.demo.service.impl.EmailTaskServiceImpl: ${EMAIL_TASK_SERVICE_LOG_LEVEL:DEBUG}
         com.example.demo.service.impl.EmailServiceImpl: ${EMAIL_SERVICE_LOG_LEVEL:DEBUG}
         com.example.demo.service.impl.JobServiceImpl: ${EMAIL_JOB_SERVICE_LOG_LEVEL:DEBUG}
       file:
         name: ${LOG_FILE_PATH:logs/application.log}
       pattern:
         file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
         console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
     ```

---

## 🎯 Components Được Log

| # | Component | Logger Name | Level | Ghi Nhật Ký |
|---|-----------|------------|-------|-----------|
| 1 | JobRecommendationScheduler | `com.example.demo.scheduler.JobRecommendationScheduler` | DEBUG | Scheduler lifecycle, task creation |
| 2 | EmailTaskWorker | `com.example.demo.scheduler.worker.EmailTaskWorker` | DEBUG | Task processing, email sending |
| 3 | EmailTaskWorkerPool | `com.example.demo.scheduler.worker.EmailTaskWorkerPool` | DEBUG | Worker pool management |
| 4 | EmailTaskService | `com.example.demo.service.impl.EmailTaskServiceImpl` | DEBUG | Task management, retries |
| 5 | EmailService | `com.example.demo.service.impl.EmailServiceImpl` | DEBUG | Email sending |
| 6 | JobService | `com.example.demo.service.impl.JobServiceImpl` | DEBUG | Job matching/recommendations |

---

## 📂 Log Files

### Main Email Flow Log
```
logs/email-recommendation-flow.log              (Current logs)
logs/email-recommendation-flow-2026-05-21.1.log.gz  (Archived, compressed)
logs/email-recommendation-flow-2026-05-20.1.log.gz  (Archived, compressed)
```

### General Application Log
```
logs/application.log                             (Current logs)
logs/application-2026-05-21.1.log.gz             (Archived, compressed)
```

---

## 🔍 Log Format

Mỗi log line:
```
2026-05-21 08:00:00.123 [EmailTaskWorker-1] DEBUG com.example.demo.scheduler.JobRecommendationScheduler - Starting job recommendation email scheduler
```

**Thành phần:**
- `2026-05-21 08:00:00.123` - Timestamp (YYYY-MM-DD HH:MM:SS.mmm)
- `[EmailTaskWorker-1]` - Thread name
- `DEBUG` - Log level (DEBUG, INFO, WARN, ERROR)
- `com.example.demo.scheduler.JobRecommendationScheduler` - Logger name (class)
- `Starting job recommendation email scheduler` - Message

---

## ⚙️ Configuration

### Via Environment Variables

```bash
# Set log levels
export LOG_LEVEL=INFO
export EMAIL_SCHEDULER_LOG_LEVEL=DEBUG
export EMAIL_WORKER_LOG_LEVEL=DEBUG
export EMAIL_TASK_SERVICE_LOG_LEVEL=DEBUG
export EMAIL_SERVICE_LOG_LEVEL=DEBUG
export EMAIL_JOB_SERVICE_LOG_LEVEL=DEBUG

# Set log file path
export LOG_FILE_PATH=logs/application.log

# Run application
mvn spring-boot:run
```

### Via application.yaml

```yaml
logging:
  level:
    root: INFO
    com.example.demo.scheduler: DEBUG
    com.example.demo.scheduler.worker: INFO
    com.example.demo.service.impl.EmailTaskServiceImpl: WARN
```

### Via .env file

Copy `.env.logging.example` và customize:
```bash
cp .env.logging.example .env.logging
```

---

## 🚀 Getting Started

### 1. Build Project
```bash
mvn clean compile
```

### 2. Run Application
```bash
mvn spring-boot:run
```

### 3. View Logs Real-time

**Unix/Linux/Mac:**
```bash
tail -f logs/email-recommendation-flow.log
```

**Windows PowerShell:**
```powershell
Get-Content logs/email-recommendation-flow.log -Tail 50 -Wait
```

### 4. Monitor Scheduler Execution (8:00 AM daily)
```bash
grep "Starting job recommendation email scheduler" logs/email-recommendation-flow.log
```

### 5. Monitor Worker Processing
```bash
grep "Processing email task" logs/email-recommendation-flow.log
```

### 6. Find Errors
```bash
grep "ERROR" logs/email-recommendation-flow.log
```

---

## 📊 Log Rotation Policy

### Rotation Triggers
- Daily at midnight (00:00)
- When file reaches 100MB

### Archive Format
- Compressed with gzip: `.log.gz`
- Pattern: `email-recommendation-flow-YYYY-MM-DD.N.log.gz`
- Example: `email-recommendation-flow-2026-05-20.1.log.gz`

### Retention
- Keep: 30 days of logs
- Max size: 3GB total
- Older logs: Auto-deleted

---

## 🔧 Customization

### Change Log Level for Specific Component

```yaml
# application.yaml
logging:
  level:
    com.example.demo.scheduler: WARN  # Less verbose
```

### Change Log File Location

```yaml
# application.yaml
logging:
  file:
    name: /var/log/email-flow.log
```

### Change Retention Period

Edit `logback-spring.xml`:
```xml
<maxHistory>14</maxHistory>  <!-- Keep 14 days instead of 30 -->
```

### Change Log Format

Edit `logback-spring.xml`:
```xml
<pattern>%d{HH:mm:ss} [%thread] %-5level - %msg%n</pattern>
```

---

## 📈 Expected Log Volume

- **Per day**: 50-100MB (tùy workload)
- **Total retention**: ~3GB (auto-cleanup)
- **Compression ratio**: ~80% (1MB → 200KB)

---

## ✅ Verification Checklist

After implementation, verify:

- [ ] `logback-spring.xml` exists at `src/main/resources/`
- [ ] `application.yaml` has `logging:` section
- [ ] Application compiles: `mvn clean compile`
- [ ] Application starts: `mvn spring-boot:run`
- [ ] `logs/` directory created
- [ ] `logs/email-recommendation-flow.log` file exists
- [ ] Logs appear when:
  - [ ] Scheduler runs (8:00 AM)
  - [ ] Workers process tasks
  - [ ] Errors occur

---

## 📚 Documentation Files

1. **`LOGGING_GUIDE.md`** - Full detailed guide (300+ lines)
2. **`EMAIL_LOGGING_QUICK_START.md`** - Quick start guide (200+ lines)
3. **`.env.logging.example`** - Environment variables examples
4. **`logback-spring.xml`** - Logback XML configuration

---

## 🎯 Log Examples

### Scheduler Execution
```
2026-05-21 08:00:00.123 [scheduler-1] INFO JobRecommendationScheduler - Starting job recommendation email scheduler
2026-05-21 08:00:00.456 [scheduler-1] INFO JobRecommendationScheduler - Processing batch 0 with 100 subscribers
2026-05-21 08:00:02.789 [scheduler-1] DEBUG EmailTaskServiceImpl - Created email task for userId: 123, taskId: 456
2026-05-21 08:00:05.012 [scheduler-1] INFO JobRecommendationScheduler - Job recommendation scheduler completed. Created: 150, Skipped: 5, Duration: 5 seconds
```

### Worker Processing
```
2026-05-21 08:00:10.123 [EmailTaskWorker-1] INFO EmailTaskWorkerPool - Fetched 10 pending tasks for processing
2026-05-21 08:00:10.456 [EmailTaskWorker-1] INFO EmailTaskWorker - Processing email task 456
2026-05-21 08:00:12.789 [EmailTaskWorker-1] DEBUG JobServiceImpl - Found 8 recommended jobs for subscriber: 567
2026-05-21 08:00:14.012 [EmailTaskWorker-1] INFO EmailServiceImpl - Sending job recommendation email to subscriber: example@gmail.com
2026-05-21 08:00:15.345 [EmailTaskWorker-1] INFO EmailTaskWorker - Successfully processed email task 456
```

### Error Handling
```
2026-05-21 08:00:20.123 [EmailTaskWorker-2] ERROR EmailTaskWorker - Error processing email task 789: SMTP connection timeout
2026-05-21 08:00:20.456 [EmailTaskWorker-2] INFO EmailTaskServiceImpl - Task 789 scheduled for retry #1 at 2026-05-21T08:01:20Z
```

---

## 🔗 Related Documentation

- **Implementation Guide**: `IMPLEMENTATION_GUIDE.md`
- **Implementation Summary**: `IMPLEMENTATION_SUMMARY.md`
- **Logging Guide**: `LOGGING_GUIDE.md`
- **Quick Start**: `EMAIL_LOGGING_QUICK_START.md`

---

## 📞 Troubleshooting

### Logs not appearing
1. Check `logs/` directory exists
2. Check file permissions
3. Check logback-spring.xml in correct location
4. Verify log level is not ERROR/OFF

### File too large
1. Reduce log level (DEBUG → INFO)
2. Reduce `maxHistory` (30 → 14)
3. Reduce `maxFileSize` (100MB → 50MB)

### Performance issues
1. Async appender already enabled (no need to change)
2. Reduce poll frequency if needed

---

## ✨ Summary

✅ **Email flow logging** to separate file: `logs/email-recommendation-flow.log`
✅ **6 components** instrumented with appropriate log levels
✅ **Production-ready** with rotation, compression, and retention
✅ **Configurable** via environment variables or application.yaml
✅ **Well-documented** with guides and examples

The system is ready to use!


