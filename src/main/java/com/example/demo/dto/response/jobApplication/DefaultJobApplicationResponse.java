package com.example.demo.dto.response.jobApplication;

import com.example.demo.model.domain.job.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultJobApplicationResponse {
    private Long id;
    private ApplicationStatus status;
    private UserInfo user;
    private JobInfo job;
    private CompanyInfo company;
    private ResumeInfo resume;
    private Instant createdAt;
    private Instant updatedAt;
    private String updateBy;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobInfo{
        private Long id;
        private String title;
        private String location;
        private List<String> skills;
        private String description;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfo {
        private Long id;
        private String email;
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyInfo{
        private Long id;
        private String name;
        private String logoUrl;
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResumeInfo{
        private String cvId;
        private String cvFileName;
        private String cvFileUrl;
    }
}
