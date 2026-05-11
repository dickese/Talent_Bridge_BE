package com.example.demo.service;

import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.company.RecruiterAdditionRequest;
import com.example.demo.dto.request.company.RecruiterRemovalRequest;
import com.example.demo.dto.response.company.CompanyRecruiterResponse;
import com.example.demo.dto.request.company.DefaultCompanyRequest;
import com.example.demo.dto.response.company.CompanyResponse;
import com.example.demo.model.domain.job.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CompanyService {
    PageResponseDto<CompanyResponse> getAllCompanies(Specification<Company> spec, Pageable pageable);
    CompanyResponse createCompany(MultipartFile logoFile, DefaultCompanyRequest request);
    CompanyResponse updateCompany(Long id, MultipartFile logoFile, DefaultCompanyRequest request, boolean isRecruiter);
    PageResponseDto<CompanyResponse> getAllCompaniesByJobCount(Pageable pageable, Specification<Company> spec);
    CompanyResponse getCompanyById(Long companyId);
    CompanyResponse getSelfCompany();
    void deleteCompanyById(Long companyId);
    List<CompanyRecruiterResponse> getSelfCompanyRecruiters();
    CompanyRecruiterResponse addCompanyRecruiter(RecruiterAdditionRequest request);
    void removeCompanyRecruiter(RecruiterRemovalRequest request);
}
