package com.example.demo.controller;


import com.example.demo.anotation.ApiMessage;
import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.resume.ResumeUpdateRequest;
import com.example.demo.dto.response.resume.DefaultResumeResponse;
import com.example.demo.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resumes")
public class ResumeController {
    private final ResumeService resumeService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Tạo resume cho người dùng hiện tại thành công")
    @PreAuthorize("hasAuthority('POST /resumes')")
    public ResponseEntity<DefaultResumeResponse> createResume
            (@RequestPart(value = "resume") MultipartFile multipartFile){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resumeService.createResume(multipartFile));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GET /resumes')")
    public PageResponseDto<DefaultResumeResponse> getAllResumes(Pageable pageable){

        return null;
    }

    @GetMapping("/me")
    @ApiMessage("Lấy tất cả resume cho người dùng hiện tại thành công")
    @PreAuthorize("hasAuthority('GET /resumes/me')")
    public PageResponseDto<DefaultResumeResponse> getAllSelfStoredResume(Pageable pageable){
        return resumeService.getAllSelfStoredResume(pageable);
    }

    @GetMapping("/me/{id}/file")
    @PreAuthorize("hasAuthority('GET /resumes/me/{id}/file')")
    public void getResumeFile(@PathVariable Long id){
        // TODO: Implement download resume file logic
    }

    @PutMapping("/me/{id}")
    @ApiMessage("Cập nhật tên resume cho người dùng hiện tại thành công")
    @PreAuthorize("hasAuthority('PUT /resumes/me/{id}')")
    public DefaultResumeResponse updateResumeName(
            @PathVariable(name = "id") Long resumeId,
            @RequestBody ResumeUpdateRequest request){

        return resumeService.updateResumeName(resumeId, request);
    }

    @DeleteMapping("/me/{id}")
    @ApiMessage("Xóa resume cho người dùng hiện tại thành công")
    @PreAuthorize("hasAuthority('DELETE /resumes/me/{id}')")
    public void deleteResumeById(@PathVariable(name = "id") Long resumeId){
        resumeService.deleteResume(resumeId);
    }
}
