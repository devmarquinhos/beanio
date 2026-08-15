package com.devmarquinhos.beanio.dto.user;

public record AuthResponse(
        String token,
        UserResponse user
) {
}
