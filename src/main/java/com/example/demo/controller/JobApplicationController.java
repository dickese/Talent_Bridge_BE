package com.example.demo.controller;

import com.example.demo.anotation.ApiMessage;
import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.company.DefaultCompanyRequest;
import com.example.demo.dto.request.jobApplication.DefaultJobApplicationRequest;
import com.example.demo.dto.request.jobApplication.UpdateJobApplicationStatusRequest;
import com.example.demo.dto.response.company.CompanyResponse;
import com.example.demo.dto.response.jobApplication.DefaultJobApplicationResponse;
import com.example.demo.model.domain.job.JobApplication;
import com.example.demo.service.JobApplicationService;
import com.turkraft.springfilter.boot.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    @PostMapping(value = "/applications", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Tạo hồ sơ ứng tuyển cho người dùng hiện tại")
    @PreAuthorize("hasAuthority('POST /applications')")
    public ResponseEntity<DefaultJobApplicationResponse> createSelfJobApplication
            (@RequestPart(value = "cv", required = false) MultipartFile multipartFile,
             @RequestPart(value = "application", required = false) DefaultJobApplicationRequest request){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(jobApplicationService.createJobApplication(request, multipartFile));
    }

    @GetMapping("/applications")
    @PreAuthorize("hasAuthority('GET /applications')")
    public PageResponseDto<DefaultJobApplicationResponse> getAllApplications(Pageable pageable){
        // TODO: Implement get all applications logic
        return null;
    }

    @GetMapping("/applications/me")
    @ApiMessage("Lấy tất cả hồ sơ ứng tuyển cho người dùng hiện tại")
    @PreAuthorize("hasAuthority('GET /applications/me')")
    public PageResponseDto<DefaultJobApplicationResponse> getAllSelfApplication(Pageable pageable, @Filter Specification<JobApplication> spec){
        return jobApplicationService.getAllJobApplicationForCurrentUser(pageable, spec);
    }

    @GetMapping("/applications/file/{id}")
    @PreAuthorize("hasAuthority('GET /applications/file/{id}')")
    public void getApplicationFile(@PathVariable Long id){
        // TODO: Implement download application file logic
    }

    @DeleteMapping("/applications/me/{jobId}")
    @PreAuthorize("hasAuthority('DELETE /applications/me/{jobId}')")
    public void deleteApplicationForJob(@PathVariable Long jobId){
        jobApplicationService.deleteJobApplicationByJobId(jobId);
    }

    @PutMapping(value = "/applications/me/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Cập nhật hồ sơ ứng tuyển cho người dùng hiện tại")
    @PreAuthorize("hasAuthority('PUT /applications/me/{id}')")
    public DefaultJobApplicationResponse updateSelfApplication
            (@PathVariable(name = "id") Long jobApplicationId,
            @RequestPart(value = "cv", required = false) MultipartFile multipartFile,
             @RequestPart(value = "application", required = false) DefaultJobApplicationRequest request){

        return jobApplicationService.updateJobApplication(jobApplicationId, request, multipartFile);
    }

    @PutMapping("/applications/{id}")
    @PreAuthorize("hasAuthority('PUT /applications/{id}')")
    public DefaultJobApplicationResponse updateApplicationStatus(@PathVariable Long id, @RequestBody UpdateJobApplicationStatusRequest request){
        return jobApplicationService.updateJobApplicationStatus(id, request);
    }

    @GetMapping("/companies/me/applications")
    @PreAuthorize("hasAuthority('GET /companies/me/applications')")
    public PageResponseDto<DefaultJobApplicationResponse> getApplicationsForCurrentCompany(Pageable pageable,@Filter Specification<JobApplication> spec){
        return jobApplicationService.getJobApplicationsByRecruiterCompany(pageable, spec);
    }

    @PutMapping("/companies/me/applications/{id}")
    @PreAuthorize("hasAuthority('PUT /companies/me/applications/{id}')")
    @ApiMessage("Cập nhật trạng thái hồ sơ ứng tuyển cho người tuyển dụng hiện tại")
    public DefaultJobApplicationResponse updateApplicationStatusByCompany(@PathVariable Long id, @RequestBody UpdateJobApplicationStatusRequest request){
        return jobApplicationService.updateJobApplicationStatus(id, request);
    }
}
