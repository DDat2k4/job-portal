package com.example.usermodule.controller;

import com.example.usermodule.service.RolePermissionService;
import com.example.usermodule.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserRoleService userRoleService;
    private final RolePermissionService rolePermissionService;

    // Gán role cho user
    @PostMapping("/users/{userId}/roles")
    public ResponseEntity<Void> assignRoles(
            @PathVariable Long userId,
            @RequestBody List<Long> roleIds
    ) {
        userRoleService.assignRolesToUser(userId, roleIds);
        return ResponseEntity.ok().build();
    }

    // Gán permission cho role
    @PostMapping("/roles/{roleId}/permissions")
    public ResponseEntity<Void> assignPermissions(
            @PathVariable Long roleId,
            @RequestBody List<Long> permissionIds
    ) {
        rolePermissionService.assignPermissionsToRole(roleId, permissionIds);
        return ResponseEntity.ok().build();
    }
}
