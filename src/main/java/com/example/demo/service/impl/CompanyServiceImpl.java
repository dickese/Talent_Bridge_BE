package com.example.demo.service.impl;

import com.example.demo.advice.exception.BadRequestException;
import com.example.demo.advice.exception.BusinessException;
import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.UploadResourceResult;
import com.example.demo.dto.request.company.RecruiterAdditionRequest;
import com.example.demo.dto.request.company.RecruiterRemovalRequest;
import com.example.demo.dto.response.company.CompanyRecruiterResponse;
import com.example.demo.dto.request.company.DefaultCompanyRequest;
import com.example.demo.dto.response.company.CompanyResponse;
import com.example.demo.model.domain.job.Company;
import com.example.demo.model.domain.job.CompanyLogo;
import com.example.demo.model.domain.user.RoleName;
import com.example.demo.model.domain.user.User;
import com.example.demo.model.domain.user.UserStatus;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.CloudService;
import com.example.demo.service.CompanyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CloudService cloudService;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Override
    public void deleteCompanyById(Long companyId) {
        companyRepository.softDeleteCompanyById(companyId);
    }

    @Override
    public PageResponseDto<CompanyResponse> getAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> page = companyRepository.findAll(spec, pageable);
        List<CompanyResponse> companies = page.getContent().stream().map(this::mapEntityToResponse).toList();



        return PageResponseDto.<CompanyResponse>builder()
                .content(companies)
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public CompanyResponse getCompanyById(Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new EntityNotFoundException("Company not found"));
        return mapEntityToResponse(company);
    }

    @Override
    public CompanyResponse getSelfCompany() {
        User user = authService.getCurrentUser();

        Company company = user.getCompany();
        if(company == null){
            throw new BusinessException("Company not found for current user");
        }

        return mapEntityToResponse(company);
    }

    @Override
    public CompanyResponse createCompany(MultipartFile logoFile, DefaultCompanyRequest request) {
        if (request == null) {
            throw new BadRequestException("Company data is required");
        }

        if(companyRepository.existsByName(request.getName())){
            throw new BadRequestException("Company name has already existed");
        }

        Company company = new Company(request.getName(),
                request.getDescription(),
                request.getAddress());

        User user = authService.getCurrentUser();

        if(RoleName.RECRUITER.name().equals(user.getRole().getName())){
            if(company.getOwner() == null){
                company.setOwner(user);
            }
            user.setCompany(company);
        }

        CompanyLogo logo = uploadLogoImage(logoFile);
        company.setLogo(logo);

        return mapEntityToResponse(companyRepository.saveAndFlush(company));
    }

    @Override
    public CompanyResponse updateCompany(Long id, MultipartFile logoFile, DefaultCompanyRequest request, boolean isRecruiter) {
        Company company;

        if(isRecruiter){
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User recruiter = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(("User not found")));

            if(Objects.isNull(recruiter.getCompany())){
                throw new EntityNotFoundException("Can't find company for this recruiter");
            }
            company = recruiter.getCompany();
        }
        else {
            company = companyRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        }

        company.setName(request.getName());
        company.setAddress(request.getAddress());
        company.setDescription(request.getDescription());

        if(logoFile != null){
            cloudService.deleteFile(company.getLogo().getFileId());
            CompanyLogo logo = uploadLogoImage(logoFile);
            company.setLogo(logo);
        }

        return mapEntityToResponse(companyRepository.saveAndFlush(company));
    }

    @Override
    public PageResponseDto<CompanyResponse> getAllCompaniesByJobCount(Pageable pageable, Specification<Company> spec) {
        Page<Company> page =  companyRepository.findAll(spec, pageable);

        List<CompanyResponse> companyResponseList = page.getContent()
                .stream()
                .map((this::mapEntityToResponse))
                .toList();

        return PageResponseDto.<CompanyResponse>builder()
                .content(companyResponseList)
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }

    @Override
    public List<CompanyRecruiterResponse> getSelfCompanyRecruiters() {
        User user = authService.getCurrentUser();
        Company company = user.getCompany();

        return company.getMembers().stream().map((member) -> {
            boolean isOwner = member.getId().equals(company.getOwner().getId());
            return mapUserToCompanyRecruiterResponse(member, isOwner);
        }).toList();
    }

    @Override
    public CompanyRecruiterResponse addCompanyRecruiter(RecruiterAdditionRequest request) {
        User user = authService.getCurrentUser();
        Company company = user.getCompany();


        if(!Objects.equals(company.getOwner().getId(), user.getId())){
            throw new BusinessException("Only company owner can add recruiters");
        }

        User newRecruiter = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + request.getEmail()));


        if(!newRecruiter.getStatus().equals(UserStatus.ACTIVE)){
            throw new BusinessException("User is not active");
        }

        if(newRecruiter.getCompany() != null){
            throw new BusinessException("User already belongs to a company");
        }

        newRecruiter.setCompany(company);
        return mapUserToCompanyRecruiterResponse(userRepository.saveAndFlush(newRecruiter), false);
    }

    @Override
    public void removeCompanyRecruiter(RecruiterRemovalRequest request) {
        String email = request.getEmail();
        User user = authService.getCurrentUser();
        Company company = user.getCompany();
        if(!Objects.equals(company.getOwner().getId(), user.getId())){
            throw new BusinessException("Only company owner can add recruiters");
        }

        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        recruiter.setCompany(null);
        userRepository.saveAndFlush(recruiter);
    }

    private CompanyResponse mapEntityToResponse(Company company){
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getAddress(),
                company.getLogo().getFileUrl(),
                company.getJobCount(),
                company.getCreatedAt(),
                company.getUpdatedAt()
        );
    }



    private CompanyLogo uploadLogoImage(MultipartFile file) {
        long MAX_LOGO_SIZE = 2 * 1024 * 1024;

        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        if (file.getSize() > MAX_LOGO_SIZE) {
            throw new BadRequestException("Logo size must be <= 2MB");
        }

        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            throw new BadRequestException("Only image files are allowed");
        }

        UploadResourceResult result = cloudService.uploadFile(file, "image");

        return new CompanyLogo(
                result.getFileId(),
                result.getFileUrl(),
                result.getFileName(),
                result.getFileSize(),
                Instant.now()
        );
    }


    private CompanyRecruiterResponse mapUserToCompanyRecruiterResponse(User user, boolean isOwner){
        return new CompanyRecruiterResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                isOwner,
                user.getStatus().equals(UserStatus.ACTIVE)
        );
    }
}
