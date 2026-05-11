package com.example.demo.model.domain.job;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "company_logos")
public class CompanyLogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileId;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
    private Instant uploadedAt;

    public CompanyLogo(String fileId, String fileUrl, String fileType,Long fileSize, Instant uploadedAt) {
        this.fileId = fileId;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.uploadedAt = uploadedAt;
        this.fileSize = fileSize;
    }
}
