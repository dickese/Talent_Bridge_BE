package com.example.demo.dto.response.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefaultCompanyResponse {
    private Long id;
    private String name;
    private String logoUrl;
}
