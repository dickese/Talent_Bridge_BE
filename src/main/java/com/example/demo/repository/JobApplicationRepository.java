package com.example.demo.repository;

import com.example.demo.model.domain.job.JobApplication;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long>, JpaSpecificationExecutor<JobApplication> {
    boolean existsByUser_IdAndJob_IdAndIsDeletedFalse(Long userId, Long jobId);

    @Modifying
    @Transactional
    @Query(
            """
                UPDATE JobApplication a
                SET a.isDeleted = true
                WHERE a.job.id = :id
            """
    )
    void softDeleteApplicationByJobId(@Param("id") Long jobId);

    default Page<JobApplication> findByCompanyId(Long companyId, Pageable pageable, Specification<JobApplication> spec) {
        Specification<JobApplication> userSpec = (root, q, cb) ->
                cb.and(
                        cb.equal(root.get("job").get("company").get("id"), companyId),
                        cb.equal(root.get("isDeleted"), false)
                );

        Specification<JobApplication> combined = userSpec.and(spec);

        return findAll(combined, pageable);
    }

    default Page<JobApplication> findByUserId(Long userId, Pageable pageable, Specification<JobApplication> spec) {
        Specification<JobApplication> userSpec = (root, query, cb) -> cb.and(
                cb.equal(root.get("user").get("id"), userId),
                cb.equal(root.get("isDeleted"), false)
        );

        Specification<JobApplication> combined = userSpec.and(spec);

        return findAll(combined, pageable);
    }
}
