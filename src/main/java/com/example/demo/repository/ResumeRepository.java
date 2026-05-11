package com.example.demo.repository;

import com.example.demo.model.domain.subscriber.Resume;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long>, JpaSpecificationExecutor<Resume> {
    boolean existsByNameAndUser_IdAndIsDeletedFalse(String name, Long userId);

    default Page<Resume> findByUserId(Long userId, Pageable pageable) {
        return findAll((root, q, cb) ->
                        cb.and(
                                cb.equal(root.get("user").get("id"), userId),
                                cb.isFalse(root.get("isDeleted"))
                        ),
                pageable
        );
    }

    @Modifying
    @Query(
        """
            UPDATE Resume r
            SET r.isDeleted = true
            WHERE r.id = :id
        """
    )
    void softDeleteResumeById(@Param("id") Long id);

    Optional<Resume> findByFileId(String cvFileId);
}
