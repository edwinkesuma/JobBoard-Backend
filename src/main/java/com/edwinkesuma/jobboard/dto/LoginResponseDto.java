package com.edwinkesuma.jobboard.dto;

public record LoginResponseDto(String message, UserDto user, String jwtToken) {
}
