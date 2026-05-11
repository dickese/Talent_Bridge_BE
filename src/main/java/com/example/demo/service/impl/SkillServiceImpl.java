package com.example.demo.service.impl;

import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.skill.DefaultSkillRequest;
import com.example.demo.dto.response.skill.DefaultSkillResponse;
import com.example.demo.model.domain.subscriber.Skill;
import com.example.demo.repository.SkillRepository;
import com.example.demo.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;

    @Override
    public DefaultSkillResponse createSkill(DefaultSkillRequest request){
        if(skillRepository.existsByName(request.getName())){
            throw new DataIntegrityViolationException("Skill already exists");
        }

        return mapEntityToDefaultResponse(skillRepository.saveAndFlush(new Skill(request.getName())));
    }

    @Override
    public PageResponseDto<DefaultSkillResponse> getAllSkills(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> page = skillRepository.findAll(spec, pageable);

        List<Skill> skills = page.getContent();
        return PageResponseDto.<DefaultSkillResponse>builder()
                .content(skills.stream().map(this::mapEntityToDefaultResponse).toList())
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public DefaultSkillResponse getSkillById(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        return mapEntityToDefaultResponse(skill);
    }

    @Override
    public DefaultSkillResponse updateSkill(Long id, DefaultSkillRequest request) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        skill.setName(request.getName());
        return mapEntityToDefaultResponse(skillRepository.saveAndFlush(skill));
    }

    @Override
    public void deleteSkill(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        skillRepository.delete(skill);
    }

    private DefaultSkillResponse mapEntityToDefaultResponse(Skill skill){
        return new DefaultSkillResponse(skill.getId(), skill.getName());
    }
}
