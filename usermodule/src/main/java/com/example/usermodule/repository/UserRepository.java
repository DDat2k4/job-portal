package com.example.usermodule.repository;

import com.example.usermodule.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Tìm theo username
    Optional<User> findByUsername(String username);

    // Tìm theo email
    Optional<User> findByEmail(String email);

    // Tìm theo phone
    Optional<User> findByPhone(String phone);

    /**
     * Lấy user cơ bản + profile name/avatar theo userId
     * Trả về Object[] để map thủ công vào UserDTO
     */
    @Query(value = """
        SELECT u.id, u.username, u.email, u.phone, u.last_login, p.name, p.avatar
        FROM users u
        LEFT JOIN user_profiles p ON u.id = p.user_id
        WHERE u.id = :userId
        """, nativeQuery = true)
    Object findUserWithProfile(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.failedAttempts = u.failedAttempts + 1 WHERE u.id = :userId")
    void increaseFailedAttempts(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.failedAttempts = 0 WHERE u.id = :userId")
    void resetFailedAttempts(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lockedUntil = :lockTime WHERE u.id = :userId")
    void lockUser(Long userId, Instant lockTime);
}


