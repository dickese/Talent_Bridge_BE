package com.example.demo.controller;

import com.example.demo.anotation.ApiMessage;
import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.job.DefaultJobRequest;
import com.example.demo.dto.response.job.DefaultJobResponse;
import com.example.demo.model.domain.job.Job;
import com.example.demo.service.JobService;
import com.turkraft.springfilter.boot.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @PostMapping("/companies/me/jobs")
    @ApiMessage("Tạo công việc cho người dùng hiện tại thành công")
    @PreAuthorize("hasAuthority('POST /companies/me/jobs')")
    public ResponseEntity<DefaultJobResponse> createJobByRecruiter(@RequestBody DefaultJobRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(jobService.createJob(request));
    }


    @PostMapping("/jobs")
    @ApiMessage("Tạo công việc thành công")
    @PreAuthorize("hasAuthority('POST /jobs')")
    public ResponseEntity<DefaultJobResponse> createJobByAdmin(@RequestBody DefaultJobRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(jobService.createJob(request));
    }

    @GetMapping("/companies/{companyId}/jobs")
    @ApiMessage("Lấy công việc theo mã công ty thành công")
    public List<DefaultJobResponse> getJobByCompany(@PathVariable(value = "companyId") Long companyId, Pageable pageable){
        return jobService.getAllJobByCompanyId(companyId, pageable);
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Lấy công việc theo mã thành công")
    public DefaultJobResponse getJobById(@PathVariable(value = "id") Long jobId){
        return jobService.getJobById(jobId);
    }

    @GetMapping("/jobs")
    @ApiMessage("Lấy tất cả công việc thành công")
    public PageResponseDto<DefaultJobResponse> getAllJob(Pageable pageable, @Filter Specification<Job> spec){
        return jobService.getAllJobs(pageable, spec);
    }

    @PutMapping("/jobs/{id}")
    @PreAuthorize("hasAuthority('PUT /jobs/{id}')")
    public DefaultJobResponse updateJob(@PathVariable Long id, @RequestBody DefaultJobRequest request){
        return jobService.updateJob(id, request);
    }

    @DeleteMapping("/jobs/{id}")
    @PreAuthorize("hasAuthority('DELETE /jobs/{id}')")
    public void deleteJob(@PathVariable Long id){
        jobService.deleteJob(id);
    }

    @GetMapping("/companies/me/jobs")
    @PreAuthorize("hasAuthority('GET /companies/me/jobs')")
    public PageResponseDto<DefaultJobResponse> getJobsByCurrentCompany(Pageable pageable){
        return jobService.getJobsByCurrentCompany(pageable);
    }

//    @PostMapping("/companies/me/jobs")
//    @PreAuthorize("hasAuthority('POST /companies/me/jobs')")
//    public ResponseEntity<DefaultJobResponse> createJobForCurrentCompany(@RequestBody DefaultJobRequest request){
//        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.createJobForCurrentCompany(request));
//    }

    @PutMapping("/companies/me/jobs/{id}")
    @PreAuthorize("hasAuthority('PUT /companies/me/jobs/{id}')")
    public DefaultJobResponse updateJobForCurrentCompany(@PathVariable Long id, @RequestBody DefaultJobRequest request){
        return jobService.updateJobForCurrentCompany(id, request);
    }

    @DeleteMapping("/companies/me/jobs/{id}")
    @PreAuthorize("hasAuthority('DELETE /companies/me/jobs/{id}')")
    public void deleteJobForCurrentCompany(@PathVariable Long id){
        jobService.deleteJobForCurrentCompany(id);
    }
}
