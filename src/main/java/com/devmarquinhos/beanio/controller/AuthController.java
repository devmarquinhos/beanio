package com.devmarquinhos.beanio.controller;

import com.devmarquinhos.beanio.dto.user.AuthRequest;
import com.devmarquinhos.beanio.dto.user.AuthResponse;
import com.devmarquinhos.beanio.dto.user.RegisterRequest;
import com.devmarquinhos.beanio.dto.user.UserResponse;
import com.devmarquinhos.beanio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        UserResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}