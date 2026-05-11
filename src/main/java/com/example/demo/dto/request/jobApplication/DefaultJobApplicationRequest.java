package com.example.demo.dto.request.jobApplication;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultJobApplicationRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private String coverLetter;

    private Long jobId;
    private Long cvId;
}
