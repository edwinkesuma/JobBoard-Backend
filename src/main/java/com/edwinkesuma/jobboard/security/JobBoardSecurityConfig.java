package com.edwinkesuma.jobboard.security;

import com.edwinkesuma.jobboard.security.filter.JwtTokenValidatorFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JobBoardSecurityConfig {

    @Qualifier("publicPaths")
    private final List<String> publicPaths;

    @Qualifier("securedPaths")
    private final List<String> securedPaths;

    @Qualifier("adminPaths")
    private final List<String> adminPaths;

    @Bean
    SecurityFilterChain customSecurityFilterChain(HttpSecurity http) {
        return http.csrf(AbstractHttpConfigurer::disable).sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> {
                    publicPaths.forEach(path -> requests.requestMatchers(path).permitAll());
                    adminPaths.forEach(path -> requests.requestMatchers(path).hasAuthority("ADMIN"));
                    securedPaths.forEach(path -> requests.requestMatchers(path).authenticated());
                    requests.anyRequest().denyAll();
                })
                .addFilterBefore(new JwtTokenValidatorFilter(publicPaths), BasicAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter()
                                    .write("{\"error\": \"Access Denied\", \"message\": \"You don't have permission to access this resource\"}");
                        })
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
