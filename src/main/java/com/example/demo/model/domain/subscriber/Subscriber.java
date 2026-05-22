package com.example.demo.model.domain.subscriber;


import com.example.demo.model.common.BaseEntity;
import com.example.demo.model.domain.job.ExperienceLevel;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "subscribers")
public class Subscriber extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Enumerated(EnumType.STRING)
    private ExperienceLevel level;

    private Double expectedSalary;

    @ManyToMany
    @JoinTable(
            name = "subscribers_skills",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills;

    public Subscriber(String email) {
        this.email = email;
    }
}
