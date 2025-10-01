package com.example.usermodule.data.pojo;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "sso")
public class SsoProperties {
    private String issuerUri;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
