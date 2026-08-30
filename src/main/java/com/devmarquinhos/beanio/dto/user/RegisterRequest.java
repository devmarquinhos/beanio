package com.devmarquinhos.beanio.dto.user;

import com.devmarquinhos.beanio.domain.enums.UserRole;

public record RegisterRequest(
        String name,
        String email,
        String password,
        String city,
        UserRole role
) {
}
