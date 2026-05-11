package com.example.demo.dto.request.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultCompanyRequest {
    private String name;
    private String description;
    private String address;
}
