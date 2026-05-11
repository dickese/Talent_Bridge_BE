package com.example.demo.service;

import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.jobApplication.DefaultJobApplicationRequest;
import com.example.demo.dto.request.jobApplication.UpdateJobApplicationStatusRequest;
import com.example.demo.dto.response.jobApplication.DefaultJobApplicationResponse;
import com.example.demo.model.domain.job.JobApplication;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobApplicationService {
    DefaultJobApplicationResponse createJobApplication(DefaultJobApplicationRequest request, MultipartFile resumePdfFile);
    DefaultJobApplicationResponse updateJobApplication(Long jobApplicationId, DefaultJobApplicationRequest request, MultipartFile resumePdfFile);
    PageResponseDto<DefaultJobApplicationResponse> getJobApplicationsByRecruiterCompany(Pageable pageable, Specification<JobApplication> spec);
    DefaultJobApplicationResponse getJobApplicationById(Long jobApplicationId);
    DefaultJobApplicationResponse updateJobApplicationStatus(Long jobApplicationId, UpdateJobApplicationStatusRequest request);
    PageResponseDto<DefaultJobApplicationResponse> getAllJobApplicationForCurrentUser(Pageable pageable, Specification<JobApplication> spec);
    void  deleteJobApplicationByJobId(Long jobId);
}
