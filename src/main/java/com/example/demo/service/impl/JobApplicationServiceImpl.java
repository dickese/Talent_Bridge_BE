package com.example.demo.service.impl;

import com.example.demo.advice.exception.BadRequestException;
import com.example.demo.advice.exception.BusinessException;
import com.example.demo.advice.exception.ForbiddenException;
import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.UploadResourceResult;
import com.example.demo.dto.request.jobApplication.DefaultJobApplicationRequest;
import com.example.demo.dto.request.jobApplication.UpdateJobApplicationStatusRequest;
import com.example.demo.dto.response.jobApplication.DefaultJobApplicationResponse;
import com.example.demo.model.domain.job.ApplicationStatus;
import com.example.demo.model.domain.job.Company;
import com.example.demo.model.domain.job.Job;
import com.example.demo.model.domain.job.JobApplication;
import com.example.demo.model.domain.subscriber.Resume;
import com.example.demo.model.domain.subscriber.Skill;
import com.example.demo.model.domain.user.User;
import com.example.demo.repository.JobApplicationRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.ResumeRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.CloudService;
import com.example.demo.service.JobApplicationService;
import com.example.demo.utils.FileHelper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {
    private final JobApplicationRepository jobApplicationRepository;
    private final ResumeRepository resumeRepository;
    private final CloudService cloudService;
    private final JobRepository jobRepository;
    private final AuthService authService;

    @Override
    @Transactional
    public DefaultJobApplicationResponse createJobApplication(
            DefaultJobApplicationRequest request,
            MultipartFile resumePdfFile
    ) {
        User user = authService.getCurrentUser();
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));

        if (jobApplicationRepository.existsByUser_IdAndJob_IdAndIsDeletedFalse(user.getId(), job.getId())) {
            throw new BusinessException("User already applied for this job");
        }

        job.validateCanApply();

        validateResumeOptions(request.getCvId(), resumePdfFile);

        JobApplication jobApplication = new JobApplication(
                request.getName(),
                request.getPhoneNumber(),
                request.getEmail(),
                request.getCoverLetter(),
                job,
                user
        );

        CvSnapshot snapshot = resolveNewCvSnapshot(request.getCvId(), resumePdfFile, user);

        jobApplication.setCV(snapshot.fileId(), snapshot.fileName(), snapshot.fileUrl());
        jobApplication.setPending();

        jobApplicationRepository.save(jobApplication);

        return mapEntityToCreateResponse(jobApplication);
    }

    private void validateResumeOptions(Long cvId, MultipartFile resumePdfFile) {
        if (cvId == null && resumePdfFile == null) {
            throw new BadRequestException("Must provide cvId or file");
        }

        if (cvId != null && resumePdfFile != null) {
            throw new BadRequestException("Can't' provide both cvId and file");
        }
    }

    @Override
    @Transactional
    public DefaultJobApplicationResponse updateJobApplication(
            Long jobApplicationId,
            DefaultJobApplicationRequest request,
            MultipartFile resumePdfFile
    ) {
        User user = authService.getCurrentUser();

        JobApplication jobApplication = jobApplicationRepository.findById(jobApplicationId)
                .orElseThrow(() -> new EntityNotFoundException("JobApplication not found"));

        if (!jobApplication.ownedBy(user)) {
            throw new ForbiddenException("No permission to update this application");
        }

        if (!ApplicationStatus.PENDING.equals(jobApplication.getStatus())) {
            throw new BusinessException("Application is already reviewing or closed");
        }

        validateResumeOptions(request.getCvId(), resumePdfFile);

        String oldFileId = jobApplication.getCvFileId();

        CvSnapshot snapshot = resolveNewCvSnapshot(request.getCvId(), resumePdfFile, user);
        jobApplication.setCV(snapshot.fileId(), snapshot.fileName(), snapshot.fileUrl());

        jobApplicationRepository.save(jobApplication);

        deleteOldCvIfNeeded(oldFileId);

        return mapEntityToCreateResponse(jobApplication);
    }

    @Override
    public DefaultJobApplicationResponse getJobApplicationById(Long jobApplicationId) {
        JobApplication application =  jobApplicationRepository.findById(jobApplicationId)
                .orElseThrow(() -> new EntityNotFoundException("Job application not found"));

        return mapEntityToCreateResponse(application);
    }

    @Override
    public PageResponseDto<DefaultJobApplicationResponse> getAllJobApplicationForCurrentUser(Pageable pageable, Specification<JobApplication> spec) {
        User user = authService.getCurrentUser();

        Page<JobApplication> page = jobApplicationRepository.findByUserId(user.getId(), pageable, spec);

        List<DefaultJobApplicationResponse> applications = page.getContent()
                .stream()
                .map(this::mapEntityToCreateResponse)
                .toList();

        return PageResponseDto.<DefaultJobApplicationResponse>builder()
                .content(applications)
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }

    @Override
    public DefaultJobApplicationResponse updateJobApplicationStatus(Long jobApplicationId, UpdateJobApplicationStatusRequest request) {

        JobApplication application = jobApplicationRepository.findById(jobApplicationId)
                .orElseThrow(() -> new EntityNotFoundException("JobApplication not found"));

        switch (request.getStatus()){
            case REVIEWING -> application.setReviewing();
            //suppose to send candidate email
            case APPROVED -> application.setApproved();
            case REJECTED -> application.setRejected();
            default -> throw new BadRequestException("Invalid status");
        }

        return mapEntityToCreateResponse(jobApplicationRepository.save(application));
    }

    @Override
    public PageResponseDto<DefaultJobApplicationResponse> getJobApplicationsByRecruiterCompany(Pageable pageable, Specification<JobApplication> spec) {
        User user = authService.getCurrentUser();
        Company company = user.getCompany();
        if(company == null){
            throw new BusinessException("Company not found for this user");
        }

        Page<JobApplication> page =  jobApplicationRepository.findByCompanyId(company.getId(), pageable, spec);
        List<DefaultJobApplicationResponse> applications = page.getContent()
                .stream()
                .map(this::mapEntityToCreateResponse)
                .toList();

        return PageResponseDto.<DefaultJobApplicationResponse>builder()
            .content(applications)
            .page(page.getNumber())
            .size(page.getSize())
            .totalPages(page.getTotalPages())
            .totalElements(page.getTotalElements())
            .build();
    }

    @Override
    public void deleteJobApplicationByJobId(Long jobId) {
        jobApplicationRepository.softDeleteApplicationByJobId(jobId);
    }

    private DefaultJobApplicationResponse mapEntityToCreateResponse(JobApplication application){
        List<String> skills = application.getJob().getSkills().stream().map(Skill::getName).toList();

        DefaultJobApplicationResponse.JobInfo jobInfo = new DefaultJobApplicationResponse.JobInfo(
                application.getJob().getId(),
                application.getJob().getName(),
                application.getJob().getLocation(),
                skills,
                application.getJob().getDescription()
        );

        DefaultJobApplicationResponse.CompanyInfo companyInfo = new DefaultJobApplicationResponse.CompanyInfo(
                application.getJob().getCompany().getId(),
                application.getJob().getCompany().getName(),
                application.getJob().getCompany().getLogo().getFileUrl()
        );

        DefaultJobApplicationResponse.ResumeInfo resumeInfo = new DefaultJobApplicationResponse.ResumeInfo(
                application.getCvFileId(),
                application.getCvFileName(),
                application.getCvFileUrl()
        );


        DefaultJobApplicationResponse.UserInfo userInfo = new DefaultJobApplicationResponse.UserInfo(
                application.getUser().getId(),
                application.getUser().getEmail()
        );


        return new DefaultJobApplicationResponse(
                application.getId(),
                application.getStatus(),
                userInfo,
                jobInfo,
                companyInfo,
                resumeInfo,
                application.getCreatedAt(),
                application.getUpdatedAt(),
                application.getModifiedBy()
        );
    }

    private CvSnapshot resolveNewCvSnapshot(Long cvId, MultipartFile file, User user) {

        if (cvId != null) {
            Resume resume = resumeRepository.findById(cvId)
                    .orElseThrow(() -> new EntityNotFoundException("Resume not found"));

            if (!resume.ownedBy(user)) {
                throw new ForbiddenException("No permission to access this resume");
            }

            return new CvSnapshot(
                    resume.getFileId(),
                    resume.getName(),
                    resume.getFileUrl()
            );
        }

        FileHelper.validatePdfResume(file);
        UploadResourceResult result = cloudService.uploadFile(file, "attachedResume");

        return new CvSnapshot(
                result.getFileId(),
                result.getFileName(),
                result.getFileUrl()
        );
    }

    private void deleteOldCvIfNeeded(String oldFileId) {
        if (oldFileId == null)
            return;

        boolean isAttachedResume = resumeRepository.findByFileId(oldFileId).isPresent();

        if (!isAttachedResume) {
            cloudService.deleteFile(oldFileId);
        }
    }

    private record CvSnapshot(
            String fileId,
            String fileName,
            String fileUrl
    ) {}
}
