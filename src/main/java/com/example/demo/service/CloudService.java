package com.example.demo.service;

import com.example.demo.dto.UploadResourceResult;
import org.springframework.web.multipart.MultipartFile;

public interface CloudService {
    UploadResourceResult uploadFile(MultipartFile file, String folderName);
    void deleteFile(String id);
}
