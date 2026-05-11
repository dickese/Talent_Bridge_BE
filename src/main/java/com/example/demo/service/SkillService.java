package com.example.demo.service;

import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.skill.DefaultSkillRequest;
import com.example.demo.dto.response.skill.DefaultSkillResponse;
import com.example.demo.model.domain.subscriber.Skill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface SkillService {
    PageResponseDto<DefaultSkillResponse> getAllSkills(Specification<Skill> spec, Pageable pageable);
    DefaultSkillResponse createSkill(DefaultSkillRequest request);
    DefaultSkillResponse getSkillById(Long id);
    DefaultSkillResponse updateSkill(Long id, DefaultSkillRequest request);
    void deleteSkill(Long id);
}
