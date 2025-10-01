package com.example.usermodule.service;

import com.example.usermodule.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    @Transactional
    public void assignRolesToUser(Long userId, List<Long> roleIds) {
        // Xóa hết role cũ
        userRoleRepository.deleteByUserId(userId);

        // Thêm mới
        for (Long roleId : roleIds) {
            userRoleRepository.addRoleToUser(userId, roleId);
        }
    }
}
