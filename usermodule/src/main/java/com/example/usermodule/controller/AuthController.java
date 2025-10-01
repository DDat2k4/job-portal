package com.example.usermodule.controller;

import com.example.usermodule.data.entity.User;
import com.example.usermodule.data.request.AuthRequest;
import com.example.usermodule.data.request.RegisterRequest;
import com.example.usermodule.data.request.RefreshRequest;
import com.example.usermodule.data.response.ApiResponse;
import com.example.usermodule.data.response.AuthResponse;
import com.example.usermodule.repository.UserRepository;
import com.example.usermodule.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Username already exists", null));
        }

        User user = new User();
        user.setUid(UUID.randomUUID());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setActive((short) 1);
        user.setFailedAttempts(0);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully!", null));
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        AuthResponse authResponse = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", authResponse));
    }

    // REFRESH TOKEN
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshRequest request) {
        AuthResponse authResponse = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed successfully", authResponse));
    }

    // LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody RefreshRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(new ApiResponse<>(true, "Logged out successfully!", null));
    }

    // LOGOUT ALL DEVICES
    @PostMapping("/logout-all/{userId}")
    public ResponseEntity<ApiResponse<Void>> logoutAll(@PathVariable Long userId) {
        authService.logoutAll(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Logged out from all devices successfully!", null));
    }

    // CHANGE PASSWORD
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            Authentication authentication,
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) {
        String username = authentication.getName();
        authService.changePassword(username, oldPassword, newPassword);
        return ResponseEntity.ok(new ApiResponse<>(true, "Password changed successfully. Please login again.", null));
    }
}
