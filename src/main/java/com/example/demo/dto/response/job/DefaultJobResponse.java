package com.example.demo.dto.response.job;

import com.example.demo.dto.request.job.DefaultJobRequest;
import com.example.demo.model.domain.job.ExperienceLevel;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefaultJobResponse {
    private Long id;
    private String name;
    private String description;
    private String location;
    private Integer quantity;
    private Instant startDate;
    private Instant endDate;
    private Double salary;
    private ExperienceLevel level;
    private CompanyDto company;
    private List<SkillDto> skills;
    private boolean active;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyDto{
        private Long id;
        private String name;
        private String address;
        private String logoUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SkillDto{
        private Long id;
        private String name;
    }

}
