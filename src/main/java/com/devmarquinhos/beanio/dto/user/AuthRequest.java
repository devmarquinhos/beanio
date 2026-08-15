package com.devmarquinhos.beanio.dto.user;

public record AuthRequest(
        String email,
        String password
) {
}
