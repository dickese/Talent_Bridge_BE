package com.example.demo.repository;

import com.example.demo.model.domain.job.Job;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> , JpaSpecificationExecutor<Job> {
    List<Job> findJobByCompany_Id(Long companyId, Pageable pageable);
}
