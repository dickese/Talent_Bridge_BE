package com.example.demo.model.domain.job;

import com.example.demo.advice.exception.BusinessException;
import com.example.demo.model.common.BaseEntity;
import com.example.demo.model.domain.subscriber.Resume;
import com.example.demo.model.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "job_applications")
public class JobApplication extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phoneNumber;
    private String email;

    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter(AccessLevel.NONE)
    private String cvFileId;

    @Setter(AccessLevel.NONE)
    private String cvFileName;

    @Setter(AccessLevel.NONE)
    private String cvFileUrl;

    private Long version;

    public JobApplication(String name, String phoneNumber, String email, String coverLetter, Job job, User user) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.coverLetter = coverLetter;
        this.job = job;
        this.user = user;
    }

    public void setCV(String cvFileId, String cvFileName, String cvFileUrl){
        if(cvFileId == null || cvFileId.equals(this.cvFileId)){
           throw new BusinessException("Invalid CV id");
        }
        if(cvFileName == null || cvFileName.isEmpty()){
            throw new BusinessException("ResumeName must not be empty");
        }
        if(cvFileUrl == null || cvFileUrl.isEmpty()){
            throw new BusinessException("ResumeName must not be empty");
        }
        if(version == null){
            this.version = 1L;
        }
        else {
            this.version = version + 1;
        }

        this.cvFileId = cvFileId;
        this.cvFileName = cvFileName;
        this.cvFileUrl = cvFileUrl;
    }

    public void setPending(){
        if(!ApplicationStatus.PENDING.equals(this.status)){
            this.status = ApplicationStatus.PENDING;
        }else {
            throw new BusinessException("This application's status is PENDING");
        }
    }

    public void setApproved(){
        if(!ApplicationStatus.APPROVED.equals(this.status)){
            this.status = ApplicationStatus.APPROVED;
        }else {
            throw new BusinessException("This application's status is APPROVAL");
        }
    }

    public void setRejected(){
        if(!ApplicationStatus.REJECTED.equals(this.status)){
            this.status = ApplicationStatus.REJECTED;
        }else {
            throw new BusinessException("This application's status is REJECTED");
        }
    }

    public void setReviewing(){
        if(!ApplicationStatus.REVIEWING.equals(this.status)){
            this.status = ApplicationStatus.REVIEWING;
        }else {
            throw new BusinessException("This application's status is REVIEWING");
        }
    }

    public boolean ownedBy(User user){
        return this.user.equals(user);
    }
}
