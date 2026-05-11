package com.example.demo.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FileHelper {
    private static final long MAX_SIZE = 1024 * 1024; // 1MB
    private static final String PDF_CONTENT_TYPE = "application/pdf";

    public static void validatePdfResume(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("File size must be less than 5MB");
        }

        // Validate content type
        if (!PDF_CONTENT_TYPE.equalsIgnoreCase(file.getContentType())) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }

        // Validate magic number
        try {
            byte[] header = new byte[4];
            file.getInputStream().read(header);

            String headerString = new String(header);
            if (!headerString.startsWith("%PDF")) {
                throw new IllegalArgumentException("Invalid PDF file");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }
    }
}
