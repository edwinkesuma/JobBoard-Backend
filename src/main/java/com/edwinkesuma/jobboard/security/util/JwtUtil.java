package com.edwinkesuma.jobboard.security.util;

import com.edwinkesuma.jobboard.constants.ApplicationConstants;
import com.edwinkesuma.jobboard.entity.JobBoardUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    
    private final Environment environment;

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    public String generateJwtToken(Authentication authentication) {
        String jwtToken;
        String
                secret =
                environment.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                        jwtSecret);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        var fetchedUser = (JobBoardUser) authentication.getPrincipal();
        jwtToken =
                Jwts.builder()
                        .issuer("JobBoard")
                        .subject("JWT Token")
                        .claim("name", fetchedUser.getName())
                        .claim("email", fetchedUser.getEmail())
                        .claim("roles", authentication.getAuthorities())
                        .issuedAt(new java.util.Date()).
                        expiration(new java.util.Date((new java.util.Date()).getTime() + 24 * 60 * 60 * 1000))
                        .signWith(secretKey).compact();
        return jwtToken;
    }
}
