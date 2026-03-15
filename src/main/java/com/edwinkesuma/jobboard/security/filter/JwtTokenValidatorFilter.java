package com.edwinkesuma.jobboard.security.filter;

import com.edwinkesuma.jobboard.constants.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenValidatorFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Qualifier("publicPaths")
    private final List<String> publicPaths;

    @Value("${JWT_SECRET}")
    private String jwtSecret;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.debug("do filter internal");

        String authHeader = request.getHeader(ApplicationConstants.JWT_HEADER);

        if (null != authHeader) {
            try {
                String jwt = authHeader.substring(7);
                Environment env = getEnvironment();

                if (null != env) {
                    String
                            secret =
                            env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                                    jwtSecret);
                    SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

                    if (null != secretKey) {
                        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload();
                        String username = String.valueOf(claims.get("email"));
                        String role = String.valueOf(claims.get("role"));
                        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
                                AuthorityUtils.commaSeparatedStringToAuthorityList(role));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }

            } catch (ExpiredJwtException exception) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token is expired");
                return;
            } catch (Exception e) {
                throw new BadCredentialsException("Invalid Token received!");
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        log.debug("should not filter");

        String path = request.getServletPath();
        return publicPaths.stream().anyMatch(publicPath -> pathMatcher.match(publicPath, path));
    }
}
