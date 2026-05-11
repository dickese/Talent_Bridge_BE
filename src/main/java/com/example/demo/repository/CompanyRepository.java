package com.example.demo.repository;


import com.example.demo.model.domain.job.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> , JpaSpecificationExecutor<Company> {
    boolean existsByName(String name);
    @Modifying
    @Query(
            """
                UPDATE Company c
                SET c.isDeleted = true
                WHERE c.id = :id
            """
    )
    void softDeleteCompanyById(@Param("id") Long id);
}
