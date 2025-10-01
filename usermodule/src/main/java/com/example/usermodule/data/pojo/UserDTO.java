package com.example.usermodule.data.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private LocalDateTime lastLogin;
    private String name;
    private String avatar;
    private Set<String> roles;
    private Set<String> permissions;
    private List<String> activeTokens;
}
