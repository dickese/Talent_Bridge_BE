package com.example.demo.service;

import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.job.DefaultJobRequest;
import com.example.demo.dto.response.company.DefaultCompanyResponse;
import com.example.demo.dto.response.job.DefaultJobResponse;
import com.example.demo.model.domain.job.Job;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface JobService {
    DefaultJobResponse createJob(DefaultJobRequest request);
    List<DefaultJobResponse> getAllJobByCompanyId(Long companyId, Pageable pageable);
    DefaultJobResponse getJobById(Long id);
    PageResponseDto<DefaultJobResponse> getAllJobs(Pageable pageable, Specification<Job> spec);
    DefaultJobResponse updateJob(Long id, DefaultJobRequest request);
    void deleteJob(Long id);
    PageResponseDto<DefaultJobResponse> getJobsByCurrentCompany(Pageable pageable);
    DefaultJobResponse createJobForCurrentCompany(DefaultJobRequest request);
    DefaultJobResponse updateJobForCurrentCompany(Long id, DefaultJobRequest request);
    void deleteJobForCurrentCompany(Long id);
}
