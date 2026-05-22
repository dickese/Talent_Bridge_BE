# 🚀 Email Logging - Quick Start Guide

## ✅ Các Thay Đổi Đã Được Thực Hiện

### 1. ✨ File Mới Được Tạo

#### `src/main/resources/logback-spring.xml`
- Cấu hình Logback XML cho Spring Boot
- Định nghĩa **EMAIL_FLOW_FILE** appender ghi logs vào `logs/email-recommendation-flow.log`
- Cấu hình 6 loggers chuyên biệt cho email flow components
- Rolling policy: Daily + 100MB size limit, giữ 30 ngày
- Async appender để tối ưu performance

#### `LOGGING_GUIDE.md`
- Hướng dẫn chi tiết toàn diện về logging system
- Gồm monitoring, troubleshooting, configuration details

### 2. 📝 File Đã Được Cập Nhật

#### `src/main/resources/application.yaml`
Thêm section `logging:` để cấu hình log levels:
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

## 📂 Log Files Location

Sau khi chạy ứng dụng, bạn sẽ tìm thấy:

```
project-root/
└── logs/
    ├── email-recommendation-flow.log              ← Email flow logs (CHÍNH)
    ├── email-recommendation-flow-2026-05-21.1.log.gz
    ├── email-recommendation-flow-2026-05-20.1.log.gz
    ├── application.log                             ← General logs
    ├── application-2026-05-21.1.log.gz
    └── application-2026-05-20.1.log.gz
```

---

## 🔍 Xem Logs Ngay Bây Giờ

### 1. Real-time Viewing (Unix/Linux/Mac)
```bash
tail -f logs/email-recommendation-flow.log
```

### 2. Real-time Viewing (Windows PowerShell)
```powershell
Get-Content logs/email-recommendation-flow.log -Tail 50 -Wait
```

### 3. Tìm ERROR logs
```bash
# Unix/Linux/Mac
grep "ERROR" logs/email-recommendation-flow.log

# Windows PowerShell
Select-String "ERROR" logs/email-recommendation-flow.log
```

### 4. Tìm logs trong vòng 1 giờ gần nhất
```bash
# Unix/Linux/Mac
tail -100 logs/email-recommendation-flow.log

# Windows PowerShell
Get-Content logs/email-recommendation-flow.log -Tail 100
```

---

## 📊 Các Components Được Ghi Log

| Component | Logger Name | Level |
|-----------|------------|-------|
| 🔔 **JobRecommendationScheduler** | `com.example.demo.scheduler.JobRecommendationScheduler` | DEBUG |
| 👷 **EmailTaskWorker** | `com.example.demo.scheduler.worker.EmailTaskWorker` | DEBUG |
| 🏊 **EmailTaskWorkerPool** | `com.example.demo.scheduler.worker.EmailTaskWorkerPool` | DEBUG |
| 📧 **EmailTaskService** | `com.example.demo.service.impl.EmailTaskServiceImpl` | DEBUG |
| 💌 **EmailService** | `com.example.demo.service.impl.EmailServiceImpl` | DEBUG |
| 💼 **JobService** | `com.example.demo.service.impl.JobServiceImpl` | DEBUG |

---

## 🎯 Expected Log Output

### Khi Scheduler Chạy (8:00 AM hàng ngày)

```
2026-05-21 08:00:00.123 [scheduler-1] INFO com.example.demo.scheduler.JobRecommendationScheduler - Starting job recommendation email scheduler
2026-05-21 08:00:00.456 [scheduler-1] INFO com.example.demo.scheduler.JobRecommendationScheduler - Processing batch 0 with 100 subscribers
2026-05-21 08:00:02.789 [scheduler-1] DEBUG com.example.demo.scheduler.JobRecommendationScheduler - Created email task for userId: 123, taskId: 456
2026-05-21 08:00:05.012 [scheduler-1] INFO com.example.demo.scheduler.JobRecommendationScheduler - Job recommendation scheduler completed. Created: 150, Skipped: 5, Duration: 5 seconds
```

### Khi Workers Xử Lý Tasks

```
2026-05-21 08:00:10.123 [EmailTaskWorker-1] INFO com.example.demo.scheduler.worker.EmailTaskWorkerPool - Fetched 10 pending tasks for processing
2026-05-21 08:00:10.456 [EmailTaskWorker-1] INFO com.example.demo.scheduler.worker.EmailTaskWorker - Processing email task 456
2026-05-21 08:00:12.789 [EmailTaskWorker-1] DEBUG com.example.demo.service.impl.JobServiceImpl - Found 8 recommended jobs for subscriber: 567
2026-05-21 08:00:14.012 [EmailTaskWorker-1] INFO com.example.demo.service.impl.EmailServiceImpl - Sending job recommendation email to subscriber: example@gmail.com
2026-05-21 08:00:15.345 [EmailTaskWorker-1] INFO com.example.demo.scheduler.worker.EmailTaskWorker - Successfully processed email task 456
```

### Khi Có Lỗi

```
2026-05-21 08:00:20.123 [EmailTaskWorker-2] ERROR com.example.demo.scheduler.worker.EmailTaskWorker - Error processing email task 789: SMTP connection timeout
2026-05-21 08:00:20.456 [EmailTaskWorker-2] INFO com.example.demo.service.impl.EmailTaskServiceImpl - Task 789 scheduled for retry #1 at 2026-05-21T08:01:20Z
```

---

## ⚙️ Configuration Options

### Via Environment Variables

```bash
# Change log level for email components
export EMAIL_SCHEDULER_LOG_LEVEL=INFO
export EMAIL_WORKER_LOG_LEVEL=WARN

# Change general log level
export LOG_LEVEL=DEBUG

# Change log file path
export LOG_FILE_PATH=/var/log/application.log
```

### Via application.yaml

```yaml
logging:
  level:
    com.example.demo.scheduler: INFO          # Từ DEBUG xuống INFO
    com.example.demo.scheduler.worker: WARN   # Từ DEBUG xuống WARN
```

---

## 🔧 Customize Logging

### Thay đổi log format

Edit `logback-spring.xml`:
```xml
<pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %msg%n</pattern>
```

### Thay đổi file location

```yaml
logging:
  file:
    name: /var/log/email-flow.log
```

### Thay đổi rotation policy

Edit `logback-spring.xml`:
```xml
<maxHistory>14</maxHistory>      <!-- Keep 14 days instead of 30 -->
<maxFileSize>50MB</maxFileSize>   <!-- 50MB instead of 100MB -->
```

---

## 📊 Log Statistics

### Kích Thước Dung Lượng
- **Per day**: ~50-100MB (tùy volume)
- **Total retained**: Max 3GB (auto-cleanup)
- **Retention period**: 30 ngày

### Compression
- Logs cũ được compress: `email-recommendation-flow-2026-05-20.1.log.gz`
- Save ~80% disk space

---

## 🚨 Troubleshooting

### Problem: Logs không xuất hiện

**Solution:**
```bash
# 1. Check logs directory exists
ls -la logs/

# 2. Check file permissions
chmod 755 logs/

# 3. Check logback-spring.xml location
ls src/main/resources/logback-spring.xml
```

### Problem: File quá lớn

**Solution:**
```yaml
logging:
  level:
    root: INFO  # Giảm verbosity
```

### Problem: Chỉ muốn ERROR logs

**Solution:**
```yaml
logging:
  level:
    com.example.demo.scheduler: ERROR
    com.example.demo.scheduler.worker: ERROR
```

---

## 📚 Complete Log Flow

```
JobRecommendationScheduler
  ├─ [08:00] Start scheduling
  ├─ Load subscribers (batch)
  ├─ Create EmailTask records
  └─ [08:05] Complete scheduling
       │
       ▼
Email Task Queue (Database)
       │
       ├─ Worker 1 polls
       ├─ Worker 2 polls
       ├─ Worker 3 polls  ← Claim task → Fetch user/job → Send email → Update status
       ├─ Worker 4 polls
       └─ Worker 5 polls
            │
            ▼
   All Logs → email-recommendation-flow.log
```

---

## ✅ Verification Checklist

- [ ] `logback-spring.xml` tạo ở `src/main/resources/`
- [ ] `application.yaml` có section `logging:`
- [ ] Chạy `mvn clean compile`
- [ ] Start ứng dụng: `mvn spring-boot:run`
- [ ] Kiểm tra `logs/` directory được tạo
- [ ] Kiểm tra `logs/email-recommendation-flow.log` file tồn tại
- [ ] Xem logs khi scheduler chạy hoặc tasks được xử lý

---

## 📞 Next Steps

1. **Start Application**
   ```bash
   mvn spring-boot:run
   ```

2. **Monitor Logs**
   ```bash
   tail -f logs/email-recommendation-flow.log
   ```

3. **Trigger Scheduler** (Wait until 8:00 AM, or modify cron for testing)

4. **Verify Email Flow** - Check logs match expected output

---

## 📖 More Information

- **Full Guide**: See `LOGGING_GUIDE.md`
- **Logback Docs**: https://logback.qos.ch/
- **Spring Logging**: https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging


