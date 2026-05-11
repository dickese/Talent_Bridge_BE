package com.example.demo.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.advice.exception.CloudUploadException;
import com.example.demo.dto.UploadResourceResult;
import com.example.demo.service.CloudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudService {
    private final Cloudinary cloudinary;

    @Override
    public UploadResourceResult uploadFile(MultipartFile file, String folderName) {
        try {

            String resourceType = detectResourceType(file);
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of(
                            "folder", folderName,
                            "resource_type", resourceType
                    )
            );
            return new UploadResourceResult(
                    uploadResult.get("public_id").toString(),
                    uploadResult.get("original_filename").toString(),
                    uploadResult.get("secure_url").toString(),
                    uploadResult.get("resource_type").toString(),
                    Long.parseLong(uploadResult.get("bytes").toString())
            );
        } catch (IOException e) {
            throw new CloudUploadException("Upload image failed");
        }
    }

    private String detectResourceType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null && contentType.startsWith("image/")) {
            return "image";
        }
        return "raw";
    }


    @Override
    public void deleteFile(String fileId) {
        String resourceType = fileId != null && fileId.startsWith("/image") ? "image" : "raw";
        try {
            Map result = cloudinary.uploader().destroy(
                    fileId,
                    Map.of("resource_type", resourceType)
            );

            if (!"ok".equals(result.get("result"))) {
                throw new CloudUploadException("Delete failed: " + result);
            }
        } catch (Exception e) {
            throw new CloudUploadException("Cloudinary delete error");
        }
    }
}
