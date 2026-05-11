package com.example.demo.dto.response.company;


import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class CompanyResponse {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String logoUrl;
    private int jobsCount;
    private Instant createdAt;
    private Instant updatedAt;
}
