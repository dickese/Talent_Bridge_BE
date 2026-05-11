package com.example.demo.dto.request.company;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RecruiterAdditionRequest {
    @NotBlank(message = "Email must not be blank")
    private String email;
}
