package com.example.usermodule.data.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_profiles")
@Data
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String name;
    private String avatar;
    private Short gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private String headline;
    private String note;
}
