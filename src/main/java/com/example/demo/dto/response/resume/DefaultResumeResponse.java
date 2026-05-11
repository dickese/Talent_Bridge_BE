package com.example.demo.dto.response.resume;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class DefaultResumeResponse {
    private Long id;
    private String fileName;
    private String fileUrl;
    private Instant createAt;
    private Instant updateAt;
}
