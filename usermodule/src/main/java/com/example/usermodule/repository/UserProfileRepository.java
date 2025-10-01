package com.example.usermodule.repository;

import com.example.usermodule.data.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    @Query("SELECT p FROM UserProfile p WHERE p.userId = :userId")
    Optional<UserProfile> findByUserId(Long userId);
}

