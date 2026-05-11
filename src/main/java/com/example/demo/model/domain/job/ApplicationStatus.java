package com.example.demo.model.domain.job;

import lombok.Getter;

@Getter
public enum ApplicationStatus {
    PENDING, REVIEWING, APPROVED, REJECTED;
}
