package com.example.demo.model.persistence.emailTask;

import com.example.demo.advice.exception.BusinessException;
import com.example.demo.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(
    name = "email_task",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "send_date"})
    },
    indexes = {
        @Index(name = "idx_email_task_status_retry", columnList = "status, next_retry_at")
    }
)
public class EmailTask extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmailTaskStatus status;

    @Column(nullable = false)
    private Integer retryCount = 0;

    @Column(name = "next_retry_at")
    private Instant nextRetryAt;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @Column(nullable = false)
    private LocalDate sendDate;


    public boolean markAsSuccess(){
        if(EmailTaskStatus.PROCESSING.equals(this.status)){
            this.status = EmailTaskStatus.SUCCESS;
            this.errorMessage = null;
            return true;
        }
        return false;
    }
}

