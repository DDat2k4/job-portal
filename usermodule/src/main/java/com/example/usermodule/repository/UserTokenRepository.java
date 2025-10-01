package com.example.usermodule.repository;

import com.example.usermodule.data.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    // Lấy tất cả token chưa bị revoke và chưa hết hạn
    @Query("SELECT t FROM UserToken t WHERE t.userId = :userId AND t.revoked = false AND t.expiresAt > CURRENT_TIMESTAMP")
    List<UserToken> findActiveTokensByUserId(@Param("userId") Long userId);

    Optional<UserToken> findByRefreshTokenAndRevokedFalse(String refreshToken);

    // Revoke tất cả token
    @Modifying
    @Transactional
    @Query("UPDATE UserToken t SET t.revoked = true WHERE t.userId = :userId AND t.revoked = false")
    void revokeAllTokensByUserId(@Param("userId") Long userId);
}
