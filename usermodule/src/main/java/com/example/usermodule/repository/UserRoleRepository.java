package com.example.usermodule.repository;

import com.example.usermodule.data.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    // Lấy tất cả tên role của user
    @Query("SELECT r.name FROM UserRole ur JOIN Role r ON ur.roleId = r.id WHERE ur.userId = :userId")
    Set<String> findRolesByUserId(@Param("userId") Long userId);

    // Lấy tất cả permission của user
    @Query("SELECT p.code FROM UserRole ur JOIN RolePermission rp ON ur.roleId = rp.roleId JOIN Permission p ON rp.permissionId = p.id WHERE ur.userId = :userId")
    Set<String> findPermissionsByUserId(@Param("userId") Long userId);

    // Xóa tất cả role của user
    @Modifying
    @Transactional
    @Query("DELETE FROM UserRole ur WHERE ur.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    // Thêm role cho user
    @Modifying
    @Transactional
    @Query("INSERT INTO UserRole(userId, roleId) VALUES (:userId, :roleId)")
    void addRoleToUser(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
