package com.example.demo.repository;

import com.example.demo.model.domain.user.Role;
import com.example.demo.model.domain.user.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    java.util.Optional<Role> findByName(String name);

    @Modifying
    @Query(value = "DELETE FROM roles_permissions WHERE permission_id = :permissionId", nativeQuery = true)
    void removePermissionFromRoles(@Param("permissionId") Long permissionId);
}
