package com.edwinkesuma.jobboard.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PathsConfig {
    @Bean(name = "publicPaths")
    public List<String> publicPaths() {
        return List.of(
                "/api/auth/register/public",
                "/api/auth/login/public"
        );
    }

    @Bean(name = "securedPaths")
    public List<String> securedPaths() {
        return List.of(
                "/api/**"
        );
    }

    @Bean(name = "adminPaths")
    public List<String> adminPaths() {
        return List.of(
        );
    }
}
