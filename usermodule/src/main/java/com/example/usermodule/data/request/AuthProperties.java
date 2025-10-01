package com.example.usermodule.data.request;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth")
@Data
public class AuthProperties {
    private int maxFailedAttempts;
    private int lockDurationMinutes;
}
