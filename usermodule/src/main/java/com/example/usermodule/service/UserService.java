package com.example.usermodule.service;

import com.example.usermodule.data.entity.UserToken;
import com.example.usermodule.data.pojo.UserDTO;
import com.example.usermodule.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserTokenRepository userTokenRepository;

    /**
     * Lấy thông tin user đầy đủ (profile + roles + permissions + tokens)
     */
    public Optional<UserDTO> getUserDetail(Long userId) {
        return userRepository.findById(userId).map(user -> {

            // profile
            var profileOpt = userProfileRepository.findByUserId(userId);
            String name = profileOpt.map(p -> p.getName()).orElse(null);
            String avatar = profileOpt.map(p -> p.getAvatar()).orElse(null);

            // roles
            Set<String> roles = userRoleRepository.findRolesByUserId(userId);
            if (roles.isEmpty()) roles.add("USER"); // default role

            // permissions
            Set<String> permissions = userRoleRepository.findPermissionsByUserId(userId);
            if (permissions == null) permissions = Collections.emptySet();

            // active refresh tokens
            List<String> activeTokens = userTokenRepository.findActiveTokensByUserId(userId)
                    .stream()
                    .map(UserToken::getRefreshToken)
                    .toList();

            // build DTO
            UserDTO dto = UserDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .lastLogin(user.getLastLogin())
                    .name(name)
                    .avatar(avatar)
                    .roles(roles)
                    .permissions(permissions)
                    .activeTokens(activeTokens)
                    .build();

            return dto;
        });
    }
}