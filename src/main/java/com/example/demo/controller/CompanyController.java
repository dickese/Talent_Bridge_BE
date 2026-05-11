package com.example.demo.controller;


import com.example.demo.anotation.ApiMessage;
import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.company.RecruiterAdditionRequest;
import com.example.demo.dto.request.company.RecruiterRemovalRequest;
import com.example.demo.dto.response.company.CompanyRecruiterResponse;
import com.example.demo.dto.request.company.DefaultCompanyRequest;
import com.example.demo.dto.response.company.CompanyResponse;
import com.example.demo.model.domain.job.Company;
import com.example.demo.service.CompanyService;
import com.turkraft.springfilter.boot.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    @ApiMessage("Lấy danh sách công ty")
    @PreAuthorize("hasAuthority('GET /companies')")
    public PageResponseDto<CompanyResponse> getAllCompanies(
            @Filter Specification<Company> spec,
            @PageableDefault(size = 9) Pageable pageable
    ) {
        return companyService.getAllCompanies(spec, pageable);
    }

    @GetMapping("/with-jobs-count")
    @ApiMessage("Lấy danh sách công ty theo số lượng công việc")
    public PageResponseDto<CompanyResponse> getAllCompaniesWithJobCount(
            @Filter  Specification<Company> spec,
            @PageableDefault(size = 9) Pageable pageable
    ) {
        return companyService.getAllCompaniesByJobCount(pageable, spec);
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy công ty theo mã thành công")
    public CompanyResponse getCompanyById(@PathVariable(name = "id") Long companyId) {
        return companyService.getCompanyById(companyId);
    }

    @GetMapping("/me")
    @ApiMessage("Lấy công ty của người dùng hiện tại")
    @PreAuthorize("hasAuthority('GET /companies/me')")
    public CompanyResponse getSelfCompany() {
        return companyService.getSelfCompany();
    }


    @GetMapping("/me/recruiters")
    @ApiMessage("Lấy tất cả người tuyển dụng theo công ty của người dùng hiện tại")
    @PreAuthorize("hasAuthority('GET /companies/me/recruiters')")
    public List<CompanyRecruiterResponse> getAllRecruitersBySelfCompany() {
        return companyService.getSelfCompanyRecruiters();
    }

    @PostMapping("/me/recruiters")
    @ApiMessage("Thêm người tuyển dụng vào công ty của người dùng hiện tại")
    @PreAuthorize("hasAuthority('POST /companies/me/recruiters')")
    public CompanyRecruiterResponse addRecruiterToSelfCompany(@RequestBody RecruiterAdditionRequest request){
        return companyService.addCompanyRecruiter(request);
    }

    @PutMapping("/me/recruiters")
    @ApiMessage("Loại bỏ người dùng khác khỏi company của người dùng hiện tại")
    @PreAuthorize("hasAuthority('PUT /companies/me/recruiters')")
    public void removeRecruiterFromSelfCompany(@RequestBody RecruiterRemovalRequest request){
        companyService.removeCompanyRecruiter(request);
    }

    @PostMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Tạo công ty cho người dùng hiện tại thành công")
    @PreAuthorize("hasAuthority('POST /companies/me')")
    public ResponseEntity<CompanyResponse> createSelfCompany
            (@RequestPart(value = "logo") MultipartFile multipartFile,
             @RequestPart(value = "company") DefaultCompanyRequest request){
        var res = companyService.createCompany(multipartFile, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Tạo công ty thành công")
    @PreAuthorize("hasAuthority('POST /companies')")
    public ResponseEntity<CompanyResponse> createCompany
            (@RequestPart(value = "logo") MultipartFile multipartFile,
             @RequestPart(value = "company") DefaultCompanyRequest request){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(companyService.createCompany(multipartFile, request));
    }

    @PutMapping(value = "/{id}")
    @ApiMessage("Cập nhật công ty thành công")
    @PreAuthorize("hasAuthority('PUT /companies/{id}')")
    public ResponseEntity<CompanyResponse> updateCompany
            (@PathVariable Long id,
            @RequestPart(value = "logo", required = false) MultipartFile multipartFile,
            @RequestPart(value = "company", required = false) DefaultCompanyRequest request){

        return ResponseEntity
                .ok()
                .body(companyService.updateCompany(id, multipartFile, request, false));
    }

    @PutMapping(value = "/me")
    @ApiMessage("Cập nhật công ty cho người dùng hiện tại thành công")
    @PreAuthorize("hasAuthority('PUT /companies/me')")
    public ResponseEntity<CompanyResponse> updateSelfCompany
            (@RequestPart(value = "logo", required = false) MultipartFile multipartFile,
             @RequestPart(value = "company", required = false) DefaultCompanyRequest request){

        return ResponseEntity
                .ok()
                .body(companyService.updateCompany(null, multipartFile, request, true));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa công ty theo mã công ty thành công")
    @PreAuthorize("hasAuthority('DELETE /companies/{id}')")
    public void deleteCompanyById(@PathVariable(name = "id") Long companyId) {
        companyService.deleteCompanyById(companyId);
    }


}
