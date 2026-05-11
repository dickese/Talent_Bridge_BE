package com.example.demo.controller;

import com.example.demo.anotation.ApiMessage;
import com.example.demo.dto.PageResponseDto;
import com.example.demo.dto.request.company.DefaultCompanyRequest;
import com.example.demo.dto.request.skill.DefaultSkillRequest;
import com.example.demo.dto.response.company.CompanyResponse;
import com.example.demo.dto.response.skill.DefaultSkillResponse;
import com.example.demo.model.domain.subscriber.Skill;
import com.example.demo.service.SkillService;
import com.turkraft.springfilter.boot.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/skills")
public class SkillController {
    private final SkillService skillService;

    @GetMapping
    @PreAuthorize("hasAuthority('GET /skills')")
    public PageResponseDto<DefaultSkillResponse> getAllSkills(Pageable pageable,@Filter Specification<Skill> spec) {
        return skillService.getAllSkills(spec, pageable);
    }

    @PostMapping
    @ApiMessage("Tạo công ty thành công")
    @PreAuthorize("hasAuthority('POST /skills')")
    public ResponseEntity<DefaultSkillResponse> createSkill(@RequestBody DefaultSkillRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(skillService.createSkill(request));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GET /skills/{id}')")
    public DefaultSkillResponse getSkillById(@PathVariable Long id){
        return skillService.getSkillById(id);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PUT /skills/{id}')")
    public DefaultSkillResponse updateSkill(@PathVariable Long id, @RequestBody DefaultSkillRequest request){
        return skillService.updateSkill(id, request);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE /skills/{id}')")
    public void deleteSkill(@PathVariable Long id){
        skillService.deleteSkill(id);
    }
}
