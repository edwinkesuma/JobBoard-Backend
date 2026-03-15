package com.edwinkesuma.jobboard.auth;

import com.edwinkesuma.jobboard.dto.*;
import com.edwinkesuma.jobboard.entity.JobBoardUser;
import com.edwinkesuma.jobboard.enums.Role;
import com.edwinkesuma.jobboard.repository.JobBoardUserRepository;
import com.edwinkesuma.jobboard.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final JobBoardUserRepository jobBoardUserRepository;

    @PostMapping("/login/public")
    public ResponseEntity<LoginResponseDto> apiLogin(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            var
                    resultAuthentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.email(),
                            loginRequestDto.password()));
            String jwtToken = jwtUtil.generateJwtToken(resultAuthentication);
            var userDto = new UserDto();
            var loggedInUser = (JobBoardUser) resultAuthentication.getPrincipal();
            BeanUtils.copyProperties(loggedInUser, userDto);
            userDto.setRole(loggedInUser.getRole().toString());
            userDto.setUserId(loggedInUser.getId());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new LoginResponseDto(HttpStatus.OK.getReasonPhrase(), userDto, jwtToken));
        } catch (BadCredentialsException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Invalid username or password");
        } catch (AuthenticationException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Authentication failed");
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred");
        }
    }

    @PostMapping("/register/public")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto registerRequestDto) {
        if (jobBoardUserRepository.existsByEmail(registerRequestDto.email())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        if (registerRequestDto.role() == null || registerRequestDto.role() == Role.ADMIN) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid role");
        }

        JobBoardUser jobBoardUser = new JobBoardUser();

        BeanUtils.copyProperties(registerRequestDto, jobBoardUser, "password", "role");

        jobBoardUser.setPassword(passwordEncoder.encode(registerRequestDto.password()));
        jobBoardUser.setRole(registerRequestDto.role());
        jobBoardUser.setCreatedAt(Instant.now());

        jobBoardUserRepository.save(jobBoardUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("User registered successfully"));
    }

    private ResponseEntity<LoginResponseDto> buildErrorResponse(HttpStatus status,
                                                                String message) {
        return ResponseEntity
                .status(status)
                .body(new LoginResponseDto(message, null, null));
    }
}
