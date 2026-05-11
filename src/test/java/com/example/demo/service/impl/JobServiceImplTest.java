//package com.example.demo.service.impl;
//
//import com.example.demo.dto.request.job.DefaultJobRequest;
//import com.example.demo.dto.response.job.DefaultJobResponse;
//import com.example.demo.model.domain.job.Company;
//import com.example.demo.model.domain.job.CompanyLogo;
//import com.example.demo.model.domain.job.ExperienceLevel;
//import com.example.demo.model.domain.job.Job;
//import com.example.demo.model.domain.subscriber.Skill;
//import com.example.demo.repository.CompanyRepository;
//import com.example.demo.repository.JobRepository;
//import com.example.demo.repository.SkillRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//class JobServiceImplTest {
//
//    @MockBean
//    private JobRepository jobRepository;
//
//    @MockBean
//    private CompanyRepository companyRepository;
//
//    @MockBean
//    private SkillRepository skillRepository;
//
//    @Autowired
//    private JobServiceImpl jobService;
//
//    @Test
//    void testCreateJob_Success() {
//        // Arrange
//
//        Instant startDate = Instant.now().plusSeconds(3600);
//        Instant endDate = Instant.now().plusSeconds(7200);
//        DefaultJobRequest request = new DefaultJobRequest(
//                "Software Engineer",
//                "Ha Noi",
//                "Develop software",
//                5,
//                startDate,
//                endDate,
//                1000.0,
//                true,
//                ExperienceLevel.ONE_YEAR,
//                new DefaultJobRequest.CompanyId(1L),
//                List.of(new DefaultJobRequest.SkillId(1L), new DefaultJobRequest.SkillId(2L))
//        );
//
//        Skill skill1 = new Skill("Java");
//        skill1.setId(1L);
//        Skill skill2 = new Skill("Spring");
//        skill2.setId(2L);
//        List<Skill> skills = List.of(skill1, skill2);
//
//        CompanyLogo logo = new CompanyLogo("fileId", "fileUrl", "image/png", 1024L, Instant.now());
//        Company company = new Company("Company A", "Description", "Address");
//        company.setId(1L);
//        company.setLogo(logo);
//
//        Job savedJob = new Job("Software Engineer", "Develop software", "Ha Noi", 5, 1000.0, startDate, endDate, true, ExperienceLevel.ONE_YEAR);
//        savedJob.setId(1L);
//        savedJob.setSkills(skills);
//        savedJob.setCompany(company);
//
//        when(skillRepository.findAllById(List.of(1L, 2L))).thenReturn(skills);
//        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
//        when(jobRepository.saveAndFlush(any(Job.class))).thenReturn(savedJob);
//
//        // Act
//        DefaultJobResponse response = jobService.createJob(request);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(1L, response.getId());
//        assertEquals("Software Engineer", response.getName());
//        assertEquals("Develop software", response.getDescription());
//        assertEquals("Ha Noi", response.getLocation());
//        assertEquals(5, response.getQuantity());
//        assertEquals(1000.0, response.getSalary());
//        assertEquals(startDate, response.getStartDate());
//        assertEquals(endDate, response.getEndDate());
//        assertEquals(ExperienceLevel.ONE_YEAR, response.getLevel());
//        assertTrue(response.isActive());
//        assertEquals(1L, response.getCompany().getId());
//        assertEquals("Company A", response.getCompany().getName());
//        assertEquals("Address", response.getCompany().getAddress());
//        assertEquals("fileUrl", response.getCompany().getLogoUrl());
//        assertEquals(2, response.getSkills().size());
//        assertEquals(1L, response.getSkills().get(0).getId());
//        assertEquals("Java", response.getSkills().get(0).getName());
//        assertEquals(2L, response.getSkills().get(1).getId());
//        assertEquals("Spring", response.getSkills().get(1).getName());
//
//        verify(skillRepository).findAllById(List.of(1L, 2L));
//        verify(companyRepository).findById(1L);
//        verify(jobRepository).saveAndFlush(any(Job.class));
//    }
//
//    @Test
//    void testCreateJob_SkillNotFound() {
//        // Arrange
//
//        DefaultJobRequest request = new DefaultJobRequest(
//                "Software Engineer",
//                "Ha Noi",
//                "Develop software",
//                5,
//                Instant.now().plusSeconds(3600),
//                Instant.now().plusSeconds(7200),
//                1000.0,
//                true,
//                ExperienceLevel.ONE_YEAR,
//                new DefaultJobRequest.CompanyId(1L),
//                List.of(new DefaultJobRequest.SkillId(1L), new DefaultJobRequest.SkillId(2L))
//        );
//
//        when(skillRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of()); // Empty list
//
//        // Act & Assert
//        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> jobService.createJob(request));
//        assertEquals("Skill not found", exception.getMessage());
//
//        verify(skillRepository).findAllById(List.of(1L, 2L));
//        verify(companyRepository, never()).findById(any());
//        verify(jobRepository, never()).saveAndFlush(any());
//    }
//
//    @Test
//    void testCreateJob_CompanyNotFound() {
//        // Arrange
//
//        DefaultJobRequest request = new DefaultJobRequest(
//                "Software Engineer",
//                "Ha Noi",
//                "Develop software",
//                5,
//                Instant.now().plusSeconds(3600),
//                Instant.now().plusSeconds(7200),
//                1000.0,
//                true,
//                ExperienceLevel.ONE_YEAR,
//                new DefaultJobRequest.CompanyId(1L),
//                List.of(new DefaultJobRequest.SkillId(1L))
//        );
//
//        Skill skill1 = new Skill("Java");
//        skill1.setId(1L);
//        List<Skill> skills = List.of(skill1);
//
//        when(skillRepository.findAllById(List.of(1L))).thenReturn(skills);
//        when(companyRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> jobService.createJob(request));
//        assertEquals("Company not found", exception.getMessage());
//
//        verify(skillRepository).findAllById(List.of(1L));
//        verify(companyRepository).findById(1L);
//        verify(jobRepository, never()).saveAndFlush(any());
//    }
//}
