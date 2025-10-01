package com.example.usermodule.repository;

import com.example.usermodule.data.entity.RolePermission;
import com.example.usermodule.data.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    // Lấy tất cả permission của role
    @Query("SELECT p FROM RolePermission rp JOIN Permission p ON rp.permissionId = p.id WHERE rp.roleId = :roleId")
    List<Permission> findPermissionsByRoleId(@Param("roleId") Long roleId);

    // Xóa tất cả permission của role
    @Modifying
    @Transactional
    @Query("DELETE FROM RolePermission rp WHERE rp.roleId = :roleId")
    void deleteByRoleId(@Param("roleId") Long roleId);

    // Thêm permission cho role
    @Modifying
    @Transactional
    @Query("INSERT INTO RolePermission(roleId, permissionId) VALUES (:roleId, :permissionId)")
    void addPermissionToRole(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    // Lấy tất cả RolePermission của role
    @Query("SELECT rp FROM RolePermission rp WHERE rp.roleId = :roleId")
    List<RolePermission> findByRoleId(@Param("roleId") Long roleId);
}
