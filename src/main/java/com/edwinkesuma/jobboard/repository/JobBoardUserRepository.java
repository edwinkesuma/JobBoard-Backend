package com.edwinkesuma.jobboard.repository;

import com.edwinkesuma.jobboard.entity.JobBoardUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobBoardUserRepository extends JpaRepository<JobBoardUser, Long> {
    Boolean existsByEmail(String email);

    Optional<JobBoardUser> findJobPortalUserByEmail(String email);
}
