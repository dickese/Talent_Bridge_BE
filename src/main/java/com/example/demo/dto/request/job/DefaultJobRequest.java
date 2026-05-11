package com.example.demo.dto.request.job;

import com.example.demo.model.domain.job.ExperienceLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultJobRequest {
    @NotBlank(message = "Tên công việc không được để trống")
    private String name;

    @NotBlank(message = "Địa điểm làm việc không được để trống")
    private String location;

    @NotBlank(message = "Mô tả công việc không được để trống")
    private String description;

    @NotNull(message = "Số lượng tuyển không được để trống")
    @Positive(message = "Số lượng tuyển phải lớn hơn 0")
    private Integer quantity;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private Instant startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private Instant endDate;

    @NotNull(message = "Mức lương không được để trống")
    @PositiveOrZero(message = "Mức lương phải lớn hơn hoặc bằng 0")
    private Double salary;

    @NotNull(message = "Trạng thái không được để trống")
    private Boolean active;

    private ExperienceLevel level;
    private CompanyId company;
    private List<SkillId> skills;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyId{
        private Long id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SkillId{
        private Long id;
    }
}
