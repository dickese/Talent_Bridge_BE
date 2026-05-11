package com.example.demo.service.impl;
import com.example.demo.advice.exception.BadRequestException;
import com.example.demo.advice.exception.BusinessException;
import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.UploadResourceResult;
import com.example.demo.dto.request.resume.ResumeUpdateRequest;
import com.example.demo.dto.response.resume.DefaultResumeResponse;
import com.example.demo.model.domain.subscriber.Resume;
import com.example.demo.model.domain.user.User;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.ResumeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.CloudService;
import com.example.demo.service.ResumeService;
import com.example.demo.utils.FileHelper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepository resumeRepository;
    private final CloudService cloudService;
    private final AuthService authService;

    @Override
    public DefaultResumeResponse createResume(MultipartFile pdfFile) {
        if(pdfFile == null || pdfFile.isEmpty()){
            throw new BadRequestException("PDF file is null or empty");
        }

        String fileName = pdfFile.getOriginalFilename();
        User user =  authService.getCurrentUser();

        if(resumeRepository.existsByNameAndUser_IdAndIsDeletedFalse(fileName, user.getId())){
            throw new BusinessException("ResumeName already exist by this user");
        }

        FileHelper.validatePdfResume(pdfFile);
        UploadResourceResult uploadResult = cloudService.uploadFile(pdfFile, "storedResume");
        Resume resume = Resume.forStoring(
                uploadResult.getFileId(),
                uploadResult.getFileUrl(),
                fileName,
                user
        );

        return mapEntityToDefaultResponse(resumeRepository.saveAndFlush(resume));
    }


    @Override
    public PageResponseDto<DefaultResumeResponse> getAllSelfStoredResume(Pageable pageable) {
        User user = authService.getCurrentUser();
        Page<Resume> page = resumeRepository.findByUserId(user.getId(), pageable);

        return PageResponseDto.<DefaultResumeResponse>builder()
                .content(page.getContent()
                        .stream()
                        .map(this::mapEntityToDefaultResponse)
                        .toList()
                )
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }

    @Override
    public DefaultResumeResponse updateResumeName(Long resumeId, ResumeUpdateRequest resumeUpdateRequest) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new EntityNotFoundException("Resume not found"));

        if(resumeRepository.existsByNameAndUser_IdAndIsDeletedFalse(resumeUpdateRequest.getName(), resume.getUser().getId())){
            throw new BusinessException("ResumeName already exist by this user");
        }
        resume.setName(resumeUpdateRequest.getName());

        return mapEntityToDefaultResponse(resumeRepository.saveAndFlush(resume));
    }

    @Override
    @Transactional
    public void deleteResume(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new EntityNotFoundException("Resume not found"));

        cloudService.deleteFile(resume.getFileId());
        resumeRepository.softDeleteResumeById(resumeId);
    }

    private DefaultResumeResponse mapEntityToDefaultResponse(Resume resume) {
        return new DefaultResumeResponse(
                resume.getId(),
                resume.getName(),
                resume.getFileUrl(),
                resume.getCreatedAt(),
                resume.getUpdatedAt()
        );
    }
}
