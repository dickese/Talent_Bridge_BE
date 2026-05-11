package com.example.demo.dto.response.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompanyRecruiterResponse {
    private Long id;
    private String email;
    private String name;
    private boolean isOwner;
    private boolean isActive;
}
