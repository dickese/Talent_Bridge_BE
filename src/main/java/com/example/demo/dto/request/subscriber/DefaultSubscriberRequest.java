package com.example.demo.dto.request.subscriber;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultSubscriberRequest {
    private List<SkillId> skillIds;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SkillId{
        private Long id;
    }
}
