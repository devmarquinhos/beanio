package com.devmarquinhos.beanio.dto.user;

public record RegisterRequest(
        String name,
        String email,
        String password,
        String city
) {
}
