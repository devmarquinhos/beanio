package com.devmarquinhos.beanio.dto.user;

import com.devmarquinhos.beanio.domain.enums.UserRole;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        String city,
        UserRole role
) {
}
