package com.example.demo.service;

import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.resume.ResumeUpdateRequest;
import com.example.demo.dto.response.resume.DefaultResumeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResumeService {
    DefaultResumeResponse createResume(MultipartFile pdfFile);
    PageResponseDto<DefaultResumeResponse> getAllSelfStoredResume(Pageable pageable);
    DefaultResumeResponse updateResumeName(Long resumeId, ResumeUpdateRequest resumeUpdateRequest);
    void deleteResume(Long resumeId);
}
