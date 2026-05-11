package com.example.demo.model.domain.job;

import com.example.demo.advice.exception.BusinessException;
import com.example.demo.model.domain.subscriber.Skill;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String location;

    private boolean active;
    private Integer quantity;

    private double salary;

    private Instant startDate;
    private Instant endDate;

    @Enumerated(EnumType.STRING)
    private ExperienceLevel level;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<JobApplication> jobApplications;

    @ManyToMany
    @JoinTable(
            name = "jobs_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills;

    public void validateCanApply(){
        if(this.startDate.isAfter(Instant.now())){
            throw new BusinessException("Job isn't opened yet");
        }

        if(this.endDate.isBefore(Instant.now())){
            throw new BusinessException("Job is overdue");
        }

        if(!this.isActive()){
            throw new BusinessException("Job is no longer active");
        }
    }

    public Job(String name, String description, String location, Integer quantity, double salary, Instant startDate, Instant endDate, boolean active, ExperienceLevel level) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.quantity = quantity;
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        this.level = level;
    }
}
