package com.example.usermodule.service;

import com.example.usermodule.data.entity.RolePermission;
import com.example.usermodule.repository.RolePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RolePermissionRepository rolePermissionRepository;

    /**
     * Gán permission cho role (overwrite)
     */
    public void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        // Xóa hết permission cũ
        rolePermissionRepository.deleteByRoleId(roleId);

        // Thêm permission mới
        List<RolePermission> newRolePerms = permissionIds.stream()
                .map(pid -> {
                    RolePermission rp = new RolePermission();
                    rp.setRoleId(roleId);
                    rp.setPermissionId(pid);
                    return rp;
                }).collect(Collectors.toList());

        rolePermissionRepository.saveAll(newRolePerms);
    }

    /**
     * Lấy permission của role
     */
    public List<Long> getPermissionsOfRole(Long roleId) {
        return rolePermissionRepository.findByRoleId(roleId)
                .stream()
                .map(RolePermission::getPermissionId)
                .toList();
    }
}
