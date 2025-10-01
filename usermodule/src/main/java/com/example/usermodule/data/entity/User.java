package com.example.usermodule.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uid;
    private String username;
    private String email;
    private String phone;

    @Column(name = "password_hash")
    private String passwordHash;

    private Short active;
    @Column(name = "failed_attempts")
    private Integer failedAttempts;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;
}