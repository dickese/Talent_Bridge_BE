package com.example.demo.dto.response.subscriber;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DefaultSubscriberResponse {
    private Long id;
    private String email;
    private List<SkillDTO> skills;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SkillDTO{
        private Long id;
        private String name;
    }
}
