package com.example.demo.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailTaskStatistics {
    private Long totalTasks;
    private Long successCount;
    private Long failedCount;
    private Long pendingCount;
}

