package com.example.demo.model.domain.subscriber;

import com.example.demo.model.common.BaseEntity;
import com.example.demo.model.domain.job.Job;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "skills")
public class Skill extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "skills")
    @EqualsAndHashCode.Exclude
    private List<Subscriber> subscribers;

    @ManyToMany
    @EqualsAndHashCode.Exclude
    private List<Job> jobs;

    public Skill(String name) {
        this.name = name;
    }
}
