package com.example.demo.dto.request.jobApplication;

import com.example.demo.model.domain.job.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateJobApplicationStatusRequest {
    @NotNull(message = "Trạng thái hồ sơ không được để trống")
    private ApplicationStatus status;
}
