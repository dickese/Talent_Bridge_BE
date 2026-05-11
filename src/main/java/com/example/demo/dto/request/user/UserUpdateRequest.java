package com.example.demo.dto.request.user;

import com.example.demo.model.common.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserUpdateRequest {
    @NotNull(message = "ID is required")
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;
    private Gender gender;
    private LocalDate dob;

    @NotBlank (message = "Phone number is required")
    private String address;
    private Long company;
    private Long roleId;
}
