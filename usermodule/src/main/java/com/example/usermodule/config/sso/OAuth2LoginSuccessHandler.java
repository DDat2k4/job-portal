package com.example.usermodule.config.sso;

import com.example.usermodule.data.entity.User;
import com.example.usermodule.data.entity.UserProfile;
import com.example.usermodule.data.entity.UserToken;
import com.example.usermodule.data.response.AuthResponse;
import com.example.usermodule.repository.UserProfileRepository;
import com.example.usermodule.repository.UserRepository;
import com.example.usermodule.repository.UserTokenRepository;
import com.example.usermodule.service.JwtService;
import com.example.usermodule.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserService userService;
    private final UserTokenRepository userTokenRepository;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        if (authentication == null || !(authentication.getPrincipal() instanceof OidcUser oidcUser)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Lấy thông tin cơ bản từ OAuth2 Provider
        String email = oidcUser.getEmail();
        String username = (String) oidcUser.getAttributes().get("name");
        String avatar = (String) oidcUser.getAttributes().get("picture");
        String provider = detectProvider(oidcUser);

        log.info("OAuth2 login success: {} via {}", email, provider);

        // Lấy hoặc tạo User
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User u = new User();
            u.setUsername(username);
            u.setEmail(email);
            return userRepository.save(u);
        });

        // Lấy hoặc tạo UserProfile
        UserProfile profile = userProfileRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    UserProfile p = new UserProfile();
                    p.setUserId(user.getId());
                    return p;
                });
        profile.setName(username);
        profile.setAvatar(avatar);
        userProfileRepository.save(profile);

        // Lấy roles & permissions
        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();
        userService.getUserDetail(user.getId()).ifPresent(detail -> {
            if (detail.getRoles() != null) roles.addAll(detail.getRoles());
            if (detail.getPermissions() != null) permissions.addAll(detail.getPermissions());
        });

        // Sinh JWT access + refresh token
        String accessToken = jwtService.generateToken(user.getUsername(), roles, permissions);
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        // Lưu refresh token vào DB
        UserToken userToken = new UserToken();
        userToken.setUserId(user.getId());
        userToken.setRefreshToken(refreshToken);
        userToken.setCreatedAt(LocalDateTime.now());
        userToken.setExpiresAt(LocalDateTime.now().plusDays(7));
        userToken.setRevoked(false);
        userTokenRepository.save(userToken);

        // Tạo response trả SPA
        AuthResponse authResponse = new AuthResponse();
        authResponse.setUsername(username);
        authResponse.setEmail(email);
        authResponse.setAvatar(avatar);
        authResponse.setProvider(provider);
        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(refreshToken);
        authResponse.setRoles(roles);
        authResponse.setPermissions(permissions);

        // Trả JSON cho SPA
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        new ObjectMapper().writeValue(response.getWriter(), authResponse);
    }

    /**
     * Detect provider theo attribute phổ biến
     */
    private String detectProvider(OidcUser oidcUser) {
        if (oidcUser.getAttributes().containsKey("sub")) return "google";
        if (oidcUser.getAttributes().containsKey("id")) return "facebook";
        return "unknown";
    }
}
