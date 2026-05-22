package com.example.demo.model.domain.job;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "company_logos")
@Builder
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
