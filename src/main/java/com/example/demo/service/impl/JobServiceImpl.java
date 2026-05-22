package com.example.demo.service.impl;

import com.example.demo.advice.exception.BadRequestException;
import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.job.DefaultJobRequest;
import com.example.demo.dto.response.company.DefaultCompanyResponse;
import com.example.demo.dto.response.job.DefaultJobResponse;
import com.example.demo.model.domain.job.Company;
import com.example.demo.model.domain.job.ExperienceLevel;
import com.example.demo.model.domain.job.Job;
import com.example.demo.model.domain.subscriber.Skill;
import com.example.demo.model.domain.subscriber.Subscriber;
import com.example.demo.model.domain.user.User;
import com.example.demo.repository.*;
import com.example.demo.service.AuthService;
import com.example.demo.service.JobService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final SkillRepository skillRepository;
    private final AuthService authService;
    private final SubscriberRepository subscriberRepository;

    @Override
    public DefaultJobResponse createJob(DefaultJobRequest request) {
        Job job = new Job(
                request.getName(),
                request.getDescription(),
                request.getLocation(),
                request.getQuantity(),
                request.getSalary(),
                request.getStartDate(),
                request.getEndDate(),
                request.getActive(),
                request.getLevel()
        );

        List<Long> skillIds = request.getSkills().stream().map(DefaultJobRequest.SkillId::getId).toList();
        List<Skill> skills = skillRepository.findAllById(skillIds);

        if(skills.isEmpty() || skills.size() != skillIds.size()){
            throw new EntityNotFoundException("Skill not found");
        }

        job.setSkills(skills);
        Company company = companyRepository.findById(request.getCompany().getId())
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        job.setCompany(company);

        return mapJobEntityToDefaultResponse(jobRepository.saveAndFlush(job));
    }


    @Override
    public List<DefaultJobResponse> getAllJobByCompanyId(Long companyId, Pageable pageable) {
        List<Job> jobs = jobRepository.findJobByCompany_Id(companyId, pageable);
        return jobs.stream().map(this::mapJobEntityToDefaultResponse).toList();
    }

    @Override
    public DefaultJobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Job not found"));
        return mapJobEntityToDefaultResponse(job);
    }

    @Override
    public PageResponseDto<DefaultJobResponse> getAllJobs(Pageable pageable, Specification<Job> spec) {
        Page<Job> page = jobRepository.findAll(spec, pageable);

        List<Job> jobs = page.getContent();
        return PageResponseDto.<DefaultJobResponse>builder()
                .content(jobs.stream().map((this::mapJobEntityToDefaultResponse)).toList())
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public DefaultJobResponse updateJob(Long id, DefaultJobRequest request) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Job not found"));
        Job updatedJob = updateEntityFromRequest(job, request);

        List<Long> skillIds = request.getSkills().stream().map(DefaultJobRequest.SkillId::getId).toList();
        List<Skill> skills = skillRepository.findAllById(skillIds);

        if(skills.isEmpty() || skills.size() != skillIds.size()){
            throw new EntityNotFoundException("Skill not found");
        }

        updatedJob.setSkills(skills);
        Company company = companyRepository.findById(request.getCompany().getId())
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        updatedJob.setCompany(company);

        return mapJobEntityToDefaultResponse(jobRepository.saveAndFlush(updatedJob));
    }

    @Override
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Job not found"));
        jobRepository.delete(job);
    }

    @Override
    public PageResponseDto<DefaultJobResponse> getJobsByCurrentCompany(Pageable pageable) {
        User user = authService.getCurrentUser();
        Company company = user.getCompany();
        if(company == null){
            throw new EntityNotFoundException("Company not found for current user");
        }

        Specification<Job> spec = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("company").get("id"), company.getId());
        Page<Job> page = jobRepository.findAll(spec, pageable);

        List<Job> jobs = page.getContent();
        return PageResponseDto.<DefaultJobResponse>builder()
                .content(jobs.stream().map(this::mapJobEntityToDefaultResponse).toList())
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public DefaultJobResponse createJobForCurrentCompany(DefaultJobRequest request) {
        User user = authService.getCurrentUser();
        Company company = user.getCompany();
        if(company == null){
            throw new EntityNotFoundException("Company not found for current user");
        }

        Job job = new Job(
                request.getName(),
                request.getDescription(),
                request.getLocation(),
                request.getQuantity(),
                request.getSalary(),
                request.getStartDate(),
                request.getEndDate(),
                request.getActive(),
                request.getLevel()
        );

        List<Long> skillIds = request.getSkills().stream().map(DefaultJobRequest.SkillId::getId).toList();
        List<Skill> skills = skillRepository.findAllById(skillIds);

        if(skills.isEmpty() || skills.size() != skillIds.size()){
            throw new EntityNotFoundException("Skill not found");
        }

        job.setSkills(skills);
        job.setCompany(company);

        return mapJobEntityToDefaultResponse(jobRepository.saveAndFlush(job));
    }

    @Override
    public DefaultJobResponse updateJobForCurrentCompany(Long id, DefaultJobRequest request) {
        User user = authService.getCurrentUser();
        Company company = user.getCompany();
        if(company == null){
            throw new EntityNotFoundException("Company not found for current user");
        }

        Job job = jobRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Job not found"));
        if(!job.getCompany().getId().equals(company.getId())){
            throw new BadRequestException("Job does not belong to current company");
        }

        Job updatedJob = updateEntityFromRequest(job, request);

        List<Long> skillIds = request.getSkills().stream().map(DefaultJobRequest.SkillId::getId).toList();
        List<Skill> skills = skillRepository.findAllById(skillIds);

        if(skills.isEmpty() || skills.size() != skillIds.size()){
            throw new EntityNotFoundException("Skill not found");
        }

        updatedJob.setSkills(skills);
        // Company remains the same

        return mapJobEntityToDefaultResponse(jobRepository.saveAndFlush(updatedJob));
    }

    @Override
    public void deleteJobForCurrentCompany(Long id) {
        User user = authService.getCurrentUser();
        Company company = user.getCompany();
        if(company == null){
            throw new EntityNotFoundException("Company not found for current user");
        }

        Job job = jobRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Job not found"));
        if(!job.getCompany().getId().equals(company.getId())){
            throw new BadRequestException("Job does not belong to current company");
        }

        jobRepository.delete(job);
    }

    @Override
    public List<Job> getJobsMatchingSubscriber(Subscriber subscriber) {
        if (subscriber == null) {
            return List.of();
        }

        List<Long> skillIds = subscriber.getSkills().stream().map(Skill::getId).toList();

        return jobRepository.findJobsMatchSubscribers(skillIds,
                subscriber.getLevel().name(),
                subscriber.getExpectedSalary(),
                2,
                5);
    }


    /**
     * Check if job experience level is compatible with subscriber level
     */
    private boolean isExperienceLevelCompatible(ExperienceLevel subscriberLevel, ExperienceLevel jobLevel) {
        // Subscriber with higher level can apply to lower level jobs
        // Subscriber with lower level cannot apply to higher level jobs
        return subscriberLevel.ordinal() >= jobLevel.ordinal();
    }

    public Job updateEntityFromRequest(Job job, DefaultJobRequest request){
        if(request.getEndDate().isBefore(Instant.now())){
            throw new BadRequestException("Thời gian kết thúc không được trước ngày hiện tại");
        }

        job.setName(request.getName());
        job.setDescription(request.getDescription());
        job.setLevel(request.getLevel());
        job.setEndDate(request.getEndDate());
        job.setStartDate(request.getStartDate());
        job.setQuantity(request.getQuantity());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());

        return job;
    }


    private DefaultJobResponse mapJobEntityToDefaultResponse(Job job){
        DefaultJobResponse.CompanyDto companyDto =
                new DefaultJobResponse.CompanyDto(job.getCompany().getId(),
                        job.getCompany().getName(),
                        job.getLocation(),
                        job.getCompany().getLogo().getFileUrl());
        List<DefaultJobResponse.SkillDto> skillDtos = job.getSkills()
                .stream()
                .map(skill -> new DefaultJobResponse.SkillDto(skill.getId(), skill.getName()))
                .toList();

        return new DefaultJobResponse(job.getId(),
                job.getName(),
                job.getDescription(),
                job.getLocation(),
                job.getQuantity(),
                job.getStartDate(),
                job.getEndDate(),
                job.getSalary(),
                job.getLevel(),
                companyDto,
                skillDtos,
                job.isActive()
        );
    }

}
