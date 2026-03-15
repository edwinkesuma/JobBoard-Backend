package com.edwinkesuma.jobboard.dto;

import com.edwinkesuma.jobboard.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(

        @NotBlank(message = "Name is required")
        @Size(min = 5, max = 100, message = "The length of the name should be between 5 and 100 character")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email address must be a valid value")
        @Size(max=150)
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 50, message = "Password length must be between 8 and 50 character")
        String password,

        @NotNull(message = "Role is required")
        Role role
) {}
