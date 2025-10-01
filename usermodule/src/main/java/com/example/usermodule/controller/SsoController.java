package com.example.usermodule.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class SsoController {

    @GetMapping("/sso/success")
    public ResponseEntity<?> onSsoSuccess(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));

        return ResponseEntity.ok(Map.of(
                "message", "SSO login success",
                "attributes", principal.getAttributes()
        ));
    }

    @GetMapping("/sso/failure")
    public ResponseEntity<?> onSsoFailure() {
        return ResponseEntity.status(401).body(Map.of("error", "SSO login failed"));
    }
}
