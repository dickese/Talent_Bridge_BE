package com.example.demo.repository;

import com.example.demo.model.domain.job.ExperienceLevel;
import com.example.demo.model.domain.job.Job;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> , JpaSpecificationExecutor<Job> {
    List<Job> findJobByCompany_Id(Long companyId, Pageable pageable);

    @Query(value = """
            SELECT j.*
            FROM jobs j
            JOIN jobs_skills js ON js.job_id = j.id
            WHERE js.skill_id IN (:skillIds)
              AND j.level = :level
              AND j.salary > 0.9 * :salary
            AND j.active = true
            GROUP BY j.id
            HAVING COUNT(DISTINCT js.skill_id) >= :minMatchSkills
            LIMIT :limit
""", nativeQuery = true)
    List<Job> findJobsMatchSubscribers(
            @Param("skillIds") List<Long> skillIds,
            @Param("level") String level,
            @Param("salary") double salary,
            @Param("minMatchSkills") int minMatchSkills,
            @Param("limit") int limit
    );
}
