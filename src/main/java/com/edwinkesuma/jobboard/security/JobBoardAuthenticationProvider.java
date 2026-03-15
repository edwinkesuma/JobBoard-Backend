package com.edwinkesuma.jobboard.security;

import com.edwinkesuma.jobboard.entity.JobBoardUser;
import com.edwinkesuma.jobboard.repository.JobBoardUserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JobBoardAuthenticationProvider implements AuthenticationProvider {

    private final JobBoardUserRepository jobBoardUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        JobBoardUser
                jobBoardUser =
                jobBoardUserRepository.findJobPortalUserByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User details not found for the user: " +
                                email));

        List<SimpleGrantedAuthority>
                authorities =
                List.of(new SimpleGrantedAuthority(jobBoardUser.getRole().toString()));
        if (passwordEncoder.matches(password, jobBoardUser.getPassword())) {
            return new UsernamePasswordAuthenticationToken(jobBoardUser, null, authorities);
        } else {
            throw new BadCredentialsException("Invalid password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
