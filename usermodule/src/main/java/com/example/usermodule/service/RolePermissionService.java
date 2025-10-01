package com.example.usermodule.service;

import com.example.usermodule.repository.RolePermissionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;

    @Transactional
    public void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        // Xóa hết permission cũ của role
        rolePermissionRepository.deleteByRoleId(roleId);

        // Thêm mới
        for (Long pid : permissionIds) {
            rolePermissionRepository.addPermissionToRole(roleId, pid);
        }
    }

    public List<Long> getPermissionsOfRole(Long roleId) {
        return rolePermissionRepository.findPermissionsByRoleId(roleId)
                .stream().map(p -> p.getId()).toList();
    }
}
