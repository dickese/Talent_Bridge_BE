package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UploadResourceResult {
    private String fileId;
    private String fileName;
    private String fileUrl;
    private String resourceType;
    private Long fileSize;
}
