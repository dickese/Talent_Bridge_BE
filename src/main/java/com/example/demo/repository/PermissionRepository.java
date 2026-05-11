package com.example.demo.repository;

import com.example.demo.model.domain.user.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PermissionRepository  extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
     boolean existsByName(String name);
     boolean existsByApiPathAndMethod(String apiPath, String method);
}
